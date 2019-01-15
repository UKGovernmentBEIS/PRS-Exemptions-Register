"use strict";

// Complex multi-level drop down selector. The <option> elements of <select> controls can have dependencies associated with them
// such that they are only shown when conditions hold for other fields. 
(function() {
    // <select> controls are grouped into "clusters". Each cluster is the set of select controls whose options have interdependencies. 
    var selectClusters = [];
    
    var addSeleniumClusterTracker = function (index) {
        var inputElement = document.createElement("input");
        inputElement.type = "hidden";
        inputElement.id = "SeleniumClusterTracker";
        inputElement.value = "true";
        document.body.appendChild(inputElement);
    } 
    addSeleniumClusterTracker(selectClusters.length);

    
    // Dependencies are supplied in one direction, typically in a hierarchical fashion by stating whether an option is allowed based on
    // whether an particular option has been chosen in the select control of the level above. But more generally, any option in any other
    // select control can be used to measure whether to display the option. By extension, if options are chosen at lower levels without
    // selecting an option from a higher level first, the lower level value limits what upper level values are consistent with it, To
    // apply those limitations, a reverse map of the dependencies is constructed. 
    var reverseDependenciesMap = {};
    
    // nds:select elements always have a data-dependencies attribute, which has a JSON serialised object containing a "data" property 
    // and a "dependencies" property.
    // The "data" property contains the values of field used by the dependencies from the tiem when the page is served. Those fields
    // may or may not appear on the current page. If a field doesn't, the value used for evaluating the dependency comes from this 
    // property. If a field does appear on this page, then the current value of the field is used instead,
    $('select[data-dependencies]').each(function() {
        var ops = {
                'EqualsComparisonOperator' : function(var1, var2) {
                    return var1 === var2;
                },
                'NotEqualsComparisonOperator' : function(var1, var2) {
                    return var1 !== var2;
                },
                'NumericGreaterThanComparisonOperator' : function(var1, var2) {
                    return var1 > var2;
                },
                'NumericGreaterThanOrEqualsComparisonOperator' : function(var1, var2) {
                    return var1 >= var2;
                },
                'NumericLessThanComparisonOperator' : function(var1, var2) {
                    return var1 < var2;
                },
                'NumericLessThanOrEqualsComparisonOperator' : function(var1, var2) {
                    return var1 <= var2;
                },
                'RegExComparisonOperator' : function(var1, var2) {
                    return new RegExp(var2).test(var1);
                }
        };
        var selectObj = $(this);
        var repass = false;
        var dataDependencies = $.parseJSON(selectObj.attr('data-dependencies'));
        
        // Dependencies are applied by hiding options, often by whether other options are hidden
        // So to start the process, reset all the options as visible and enabled.
        var initialiseOptionsAsVisible = function() {
            selectObj.find('option').filter(function () { this.disabled = false; });
        }
        
        // Runs the dependencies evaluation for this select control.
        var applyDependencies = function() {
            var pathValues = {};
            var pathEnabledValues = {};
            var getPathValue = function(path) {
                var fieldControl = $('[name="'+path+'"]');
                if (fieldControl.length > 0) {
                    if (fieldControl.attr("data-value-override") != null) {
                        return fieldControl.attr("data-value-override");
                    }
                    return fieldControl.val()||'';
                }
                return dataDependencies.data[valueSource.path];
            };
            var getPathEnabledValues = function(path) {
                var fieldControl = $('[name="'+path+'"]');
                var enabledValues = {};
                fieldControl.find('option').filter(function () { return !this.disabled; }).each(function() {
                    enabledValues[this.value] = true;
                });
                return enabledValues;
            };
            // gets the value for a dependency. This can be a literal value, a value from a field control, a value previously 
            // captured and in the model, or the empty string otherwise.
            // if the value source value has a path, but there is no field on the form that matches the path, then the value 
            // is taken from the dataDependencies.data property, which is provided attached to the select element when the page 
            // is served. If there is a field on the form that matches the path, then the field value is taken from the field's 
            // data-value-override attribute if it's not null, or otherwise the current value of the field. 
            var getValue = function(valueSource) {
                if (valueSource.value) {
                    return valueSource.value;
                } else if (valueSource.path) {
                    var value = pathValues[valueSource.path];
                    if (value === void 0) {
                        value = getPathValue(valueSource.path);
                        pathValues[valueSource.path] = value;
                    }
                    return value;
                } else {
                    return '';
                }
            };
            var isOptionEnabled = function(valueSource, matchVal) {
                var enabledValues = pathEnabledValues[valueSource.path];
                if (enabledValues === void 0) {
                    enabledValues = getPathEnabledValues(valueSource.path);
                    pathEnabledValues[valueSource.path] = enabledValues;
                }
                return enabledValues[matchVal] !== void 0;
            };
            var ruleEvaluate = function(rule) {

                var alternates = rule.alternates;
                var every = rule.every;
                if (alternates) {
                    return (function doAlternates() {
                        for (var i = 0, len = alternates.length ; i < len ; i++) {
                            if (ruleEvaluate(alternates[i])) {
                                return true;
                            }
                        }
                        return false;
                    })();
                } else if (every) {
                    return (function doEvery() {
                        for (var i = 0, len = every.length ; i < len ; i++) {
                            if (! ruleEvaluate(every[i])) {
                                return false;
                            }
                        }
                        return true;
                    })();
                } else {
                    return (function doSingle() {
                        // enable if either the value is set to a value which is not '', or the matched value option is not disabled if the value is ''
                        var val1 = getValue(rule.value1);
                        var val2 = getValue(rule.value2);
                        if (rule.value1.path && val1 === '' && val2 !== '') {
                            return isOptionEnabled(rule.value1, val2);
                        }
                        if (rule.value2.path && val2 === '' && val1 !== '') {
                            return isOptionEnabled(rule.value2, val1);
                        }
                        return ops[rule.comparator.typeName](val1, val2) ;
                    })();
                }
            };
            var optionHideShow = function (option, enabled) {
                option.style.display = enabled ? 'block' : 'none';
                if (option.disabled === enabled) {
                    option.disabled = !enabled;
                    repass = true;
                }
            };
            // Loop over every option in the select, hiding those options where the dependency rules do not hold.
            var reverseRuleSet = reverseDependenciesMap[selectObj.attr('name')];
            selectObj.find('option').each(function() {
                var option = $(this);
                var optionDetails = option.val();
                var ruleSet = dataDependencies.dependencies[optionDetails];
                var enabled = ruleSet ? ruleEvaluate(ruleSet) : true;
                if (enabled) {
                    if (reverseRuleSet) {
                        if (reverseRuleSet[optionDetails]) {
                            enabled = ruleEvaluate(reverseRuleSet[optionDetails]);
                        } else {
                            var endsWith = function(pattern) {
                                var d = this.length - pattern.length;
                                return d >= 0 && this.lastIndexOf(pattern) === d;
                            };
                            // default to enable for "Please Select" and "Other" and disable otherwise.
                            // test for "*_Other" is a bit of a hack, but a case of using convention over configuration for questionnaire definition convenience
                            enabled = (optionDetails === '') || endsWith.call(optionDetails, "_Other");
                        }
                    }
                }
                optionHideShow(this, enabled);
            });
            
            // If the current option is no longer visible, change the value to the first visible value, typically the "please select" value.
            if (selectObj.val() === null) {
                var options = selectObj.find('option:not([disabled])');
                selectObj.val(options.val());
            }
        }
        
        // handler for when the user selects a different value for this select control. All the options in all the selects in ant cluster
        // to which this select control is a member have their visibility recomputed,
        var clusterChange = function(evName) {
            repass = true;      // visibility of options depends on the visibility of options in other selects, which
                                // can get processed in any order, so if the visibility of an option changes, another 
                                // cycle of visibility computation is required.
            var maxIter = 10;   // Block infinite repasses, which could happen if there are conflicting cyclic dependencies 
            
            // Reset the selects in the cluster
            $.each(selectClusters, function(clusterIndex, cluster) {
                if ($.inArray(selectObj[0].name, cluster) > -1) {
                    $.each(cluster, function(fieldControlNameIndex, fieldControlName) {
                        var fieldControl = $('[name="'+ fieldControlName+'"]');
                        if (fieldControl.length > 0) {
                            fieldControl.trigger("cluster:beforeApplyDependencies");
                        }
                    });
                }
            });
            
            // Iterate over the selects, applying disabled and hiding options which don't meet the dependencies. 
            // Multiple passes maybe required since the dependencies may be in any order.
            while (repass && --maxIter >= 0) {
                repass = false;
                $.each(selectClusters, function(clusterIndex, cluster) {
                    if ($.inArray(selectObj[0].name, cluster) > -1) {
                        $.each(cluster, function(fieldControlNameIndex, fieldControlName) { 
                            var fieldControl = $('[name="'+fieldControlName+'"]');
                            if (fieldControl.length > 0) {
                                fieldControl.trigger("cluster:applyDependencies");
                            }
                            if (repass) {
                                return false;
                            }
                        });
                    }
                });
            }
            
            // set a flag on the SeleniumClusterTracker object that selenium can test to tell when the options
            // have been recalculated. The Selenium tests set the value to false. before changing the
            // select options.
            document.getElementById("SeleniumClusterTracker").value = "true";
        };
        
        // Initialise the clusters. Where select controls on the page have dependencies on other select fields, those 
        // fields should be added to the same cluster. But a page may contain more than one cluster when select controls 
        // are completely independent of one another. 
        (function() {
            var usedFields = [ selectObj.attr("name") ];
            for (var optionDetails in dataDependencies.dependencies) {
                var attachUsedFields = function(usedFields, valueSource) {
                    if (valueSource.path && ($.inArray(valueSource.path, usedFields) === -1)) {
                        usedFields.push(valueSource.path);
                    }
                };
                var collateUsedFields = function(usedFields, rule) {
                    var alternates = rule.alternates;
                    var every = rule.every;
                    if (alternates) {
                        for (var i = 0, len = alternates.length ; i < len ; i++) {
                            collateUsedFields(usedFields, alternates[i]);
                        }
                    } else if (every) { 
                        for (var i = 0, len = every.length ; i < len ; i++) {
                            collateUsedFields(usedFields, every[i]);
                        }
                    } else {
                        attachUsedFields(usedFields, rule.value1);
                        attachUsedFields(usedFields, rule.value2);
                    }
                };
                collateUsedFields(usedFields, dataDependencies.dependencies[optionDetails]);
            }
            var newCluster = true;
            $.each(selectClusters, function(clusterIndex, cluster) {
                var found = false;
                for (var i = 0, len = usedFields.length ; i < len ; i++) {
                    if ($.inArray(usedFields[i], cluster) > -1) {
                        found = true;
                    }
                }
                if (found) {
                    for (var i = 0, len = usedFields.length ; i < len ; i++) {
                        if ($.inArray(usedFields[i], cluster) === -1) {
                            cluster.push(usedFields[i]);
                        }
                    }
                    newCluster = false;
                }
            });
            if (newCluster) {
                var cluster = [];
                for (var i = 0, len = usedFields.length ; i < len ; i++) {
                    cluster.push(usedFields[i]);
                }
                selectClusters.push(cluster);
            }
        })();
        
        // Compute the reverse dependencies. These follow essentially the same structure are normal dependencies, so they can be evaluated using the same evaluation code.
        (function() {
            // Reverse dependencies only handles field values, which are indicated by having a "path" property, and literal values, which have a "value" property
            var getValueType = function(valueSource) {
                if (valueSource === undefined) {
                    return 'undefined';
                }
                if (valueSource.value) {
                    return 'literal';
                } else if (valueSource.path) {
                    var fieldControl = $('[name="'+valueSource.path+'"]');
                    if (fieldControl.length > 0) {
                        return 'path';
                    }
                    return 'fixed';
                } else {
                    return 'literal';
                }
            };
            
            // Add in a reversed dependency, creating the structure containers as necessary
            var addReversed = function(optionDetails, pathValueSource, literalValueSource) {
                var reverseSelectDependencies = reverseDependenciesMap[pathValueSource.path];
                if (reverseSelectDependencies === undefined) {
                    reverseSelectDependencies = {};
                    reverseDependenciesMap[pathValueSource.path] = reverseSelectDependencies;
                }
                var reverseOptionDependencies = reverseSelectDependencies[literalValueSource.value];
                if (reverseOptionDependencies === undefined) {
                    reverseOptionDependencies = {};
                    reverseSelectDependencies[literalValueSource.value] = reverseOptionDependencies;
                }
                var alternates = reverseOptionDependencies['alternates'];
                if (alternates === undefined) {
                    alternates = [];
                    reverseOptionDependencies['alternates'] = alternates;
                }
                alternates.push({'value1':{'path':selectObj.attr('name')}, 'value2':{'value':optionDetails}, 'comparator':{'typeName':"EqualsComparisonOperator"}}); 
            }
            
            // extract the reverse dependencies of a single normal dependency rule
            var addReversedDependencies = function(optionDetails, rule, tiedFields) {
                var alternates = rule.alternates;
                var every = rule.every;
                if (alternates) {
                    for (var i = 0, len = alternates.length ; i < len ; i++) {
                        addReversedDependencies(optionDetails, alternates[i], tiedFields);
                    }
                } else if (every) { 
                    for (var i = 0, len = every.length ; i < len ; i++) {
                        addReversedDependencies(optionDetails, every[i], tiedFields);
                    }
                } else {
                    var type1 = getValueType(rule.value1);
                    if ((type1 === 'path') && (getValueType(rule.value2) === 'literal')) {
                        addReversed(optionDetails, rule.value1, rule.value2);
                        if ($.inArray(rule.value1.path, tiedFields) == -1) {
                            tiedFields.push(rule.value1.path);
                        }
                    } else if ((type1 === 'literal') && (getValueType(rule.value2) === 'path')) {
                        addReversed(optionDetails, rule.value2, rule.value1);
                        if ($.inArray(rule.value2.path, tiedFields) == -1) {
                            tiedFields.push(rule.value2.path);
                        }
                    }
                }
            }
            
            // build the reverse dependencies from the forward dependencies
            var tiedFields = [];
            for (var optionDetails in dataDependencies.dependencies) {
                addReversedDependencies(optionDetails, dataDependencies.dependencies[optionDetails], tiedFields);
            }
            // sometimes a select in the cluster might have options which are not referred to by forward dependencies
            // in this case, we need to add the reverse dependencies for them and allow them to be displayed when
            // the dependent field is set to unselected
            $.each(tiedFields, function() {
                var field = this;
                var fieldControl = $('[name="'+field+'"]');
                fieldControl.find('option').each(function() {
                    addReversed('', {path:field}, {value:this.value});
                });
            });
            
        })();
        
        // Compute the initial visible and enable state of the options of this select control 
        applyDependencies();
        
        // Attach the change event handlers to the select control.
        selectObj.on('cluster:beforeApplyDependencies', initialiseOptionsAsVisible); 
        selectObj.on("cluster:applyDependencies", applyDependencies);
        selectObj.on("change", function() { selectObj.attr("data-value-override", null); clusterChange("change"); });
        // before displaying the drop down for this select control, recompute the possible options as if the value
        // of this select control was "please select", so that its current value does not influence what options
        // can be selected within this control
        //selectObj.on("focus", function() { selectObj.attr("data-value-override", ''); clusterChange("focus"); });
        //selectObj.on("blur", function() { selectObj.attr("data-value-override", null); clusterChange("blur"); });
    });
})();
