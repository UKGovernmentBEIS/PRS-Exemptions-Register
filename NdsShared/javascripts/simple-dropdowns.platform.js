"use strict";

//Simple multi-level drop down selector. There's a select control for each option in the parent select element, Each is initially hidden and 
// is displayed when the selected parent value matches the child select control
(function() {
    var fieldContainers = {};
    var childSelects = $('[data-parent-path]');
    var parentSelects = {};
    childSelects.each(function() {
        var child = $(this);
        $('[name="'+child.attr('data-parent-path')+'"]').on("change", function() {
            child.closest('.subSelect').toggleClass('js-hidden', $(this).val() !== child.attr('data-parent-code'));
            child.prop('disabled', !($(this).val() === child.attr('data-parent-code')));
            if ($(this).val() !== child.attr('data-parent-code')) {
                child.val("");
            }
        }).each(function() {
            child.prop('disabled', !($(this).val() === child.attr('data-parent-code')));
            if ($(this).val() !== child.attr('data-parent-code')) {
                child.val("");
            }
            fieldContainers[child.attr('data-field-id')] = child.attr('data-parent-path');
            
            // collect up the child select controls as a set associated with the parent selector. 
            var parentSelect = parentSelects[child.attr('data-parent-path')];
            if (parentSelect === undefined) {
                parentSelects[child.attr('data-parent-path')] = {'fieldId': child.attr('data-field-id'), children:[]};
                parentSelect = parentSelects[child.attr('data-parent-path')];
            }
            parentSelect.children.push(child.closest('.subSelect'));
        }); 
    });
    
    $.each(parentSelects, function(key, childColl) {
        var fieldId = childColl.fieldId;
        var childList = childColl.children;
        $('[name="'+key+'"]').on("change", function() {
            // if all the child selects are hidden, the hide the wrapping field so that the 
            // label is hidden too. 
            var display = false;
            $.each(childList, function() {
                if (! $(this).hasClass('js-hidden')) {
                    display = true;
                    return false;
                }
            });
            $('[id="'+fieldId+'"]').toggleClass('js-hidden', !display);
        }).each(function() {
            // if all the child selects are initially hidden, the hide the wrapping field so that the 
            // label is hidden too. 
            var display = false;
            $.each(childList, function() {
                if (! $(this).hasClass('js-hidden')) {
                    display = true;
                    return false;
                }
            });
            $('[id="'+fieldId+'"]').toggleClass('js-hidden', !display);
        });
    });
    
    $.each(fieldContainers, function(key, fieldContainer) {
        $('[name="'+ fieldContainer +'"]').on("change", function() {
            var container = $('[id="'+ key +'"]');
            if (container.find('select:not([hidden])').length === 0) {
                container.parent().attr('hidden', 'hidden');
            } else {
                container.parent().removeAttr('hidden');
            } 
        });
    });
    
    // The list of options for many select dropdowns are the same on every rendering,
    // only the selected option changes. Sometimes, a page may contain the same 
    // selection list multiple times. If browser session storage is available, 
    // (most modern browsers support this) the option lists can be stored on the browser  
    // so there's no need for the server to send the full list every time. Instead it can
    // just send the name of which list to use, and the selected option from the list
    // reducing the page size.
    if (window.sessionStorage) {
        // When storing the list of options, one option may be selected on the
        // source set, so create a copy and deselect all options.
        var removeSelection = function(selectEl) {
            if (! selectEl.cloneNode) {
                return null;
            }
            var cloned = selectEl.cloneNode(true);
            cloned.value = null;
            return cloned;
        }
        var idx = 0;
        var forms = $("form");
        // create form fields to tell the server about the options lists that are
        // currently stored on the browser, so the server knows it does not need to
        // send them again
        forms.each(function() { 
            for ( ; idx < sessionStorage.length ; idx++) {
                if (sessionStorage.key(idx).startsWith('refdataset-')) {
                    var form = this;
                    // Keep the ui responsive by adding the fields in a timeout handler 
                    setTimeout((function(idx) {
                        return function() {
                            var refDataSet = sessionStorage.key(idx).substring(11);
                            var inp = document.createElement("input");
                            inp.setAttribute("name", "refDataSets["+idx+"]");
                            inp.setAttribute("value", refDataSet);
                            inp.setAttribute("type", "hidden");
                            form.insertBefore(inp, form.firstChild);
                        };
                    })(idx), 100);
                }
            }   
        });
        // for each select list sent from the server, it either contains a list of
        // options, or a "reuse" instruction to use the list that the browser has
        // and just set the selected option. 
        $('select[data-refdataset]').each(function() {
            var refDataSet = this.getAttribute('data-refdataset');
            if (this.hasAttribute("data-reuse")) {
                var optionsHTML = sessionStorage.getItem('refdataset-' + refDataSet);
                this.innerHTML = optionsHTML;
                this.value = this.getAttribute("data-reuse");
            } else {
                // if the server has sent the full list of options, store them in the 
                // session storage, and add a input field to tell the server that next 
                // time, it doesn't need to resend the list.
                var selectEl = this;
                setTimeout(function() {
                    sessionStorage.setItem('refdataset-' + refDataSet, removeSelection(selectEl).innerHTML);
                    forms.each(function() { 
                        var inp = document.createElement("input");
                        inp.setAttribute("name", "refDataSets["+idx+"]");
                        inp.setAttribute("value", refDataSet);
                        inp.setAttribute("type", "hidden");
                        this.insertBefore(inp, this.firstChild);
                    });
                }, 100);
            }
        });
        
    }
    
})();
