import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/** 
 * Read in grepped log data and find some of the slowest calls to/from back office.
 * Example grep : more NdsBeisEsb.log.2 | grep -A2 'Inbound\|Outbound' | grep -v "\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-\-"
 */
public class BackOfficeCallsLogAnalyzer {
	
	// matched outbound/inbound data
	List<LogData> processedData = new ArrayList<LogData>();
	
	// outbound data waiting for inbound match, based on ID
	Map<Integer, LogData> processingData = new HashMap<Integer, LogData>();
		
	// pattern matchers
	Pattern idPattern = Pattern.compile("ID: (\\d+)");
	Pattern outboundPattern = Pattern.compile(".*Outbound Message");
	Pattern inboundPattern = Pattern.compile(".*Inbound Message");
	Pattern dissectTimePattern = Pattern.compile("(\\d+):(\\d+):(\\d+)");
	Pattern datePattern, timePattern;
	
	public static void main (String args[]) {
		String dateRegex = "\\d\\d Jul 2018";
		BackOfficeCallsLogAnalyzer an = new BackOfficeCallsLogAnalyzer("c:/scratch/log1", dateRegex);
	}
	
	/** Read the file and analyze. */
	BackOfficeCallsLogAnalyzer(String filename, String targetDateRegex) {
		
		// setup the patterns based on the date
		datePattern = Pattern.compile(targetDateRegex + ".*");
		timePattern = Pattern.compile(targetDateRegex + " (\\d+:\\d+:\\d+).*");
		
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line;
			
            while ((line = reader.readLine()) != null) {
            	//System.out.println("NEW LOG DATA");
            	LogData logData = new LogData();
                do {
                	logData.addLine(line);
                	line = reader.readLine();
                } while ((line != null) && (! "--".equals(line)));
                
                // either store or merge the log data
                if (processingData.containsKey(logData.getId())) {
                	LogData otherOne = processingData.remove(logData.getId());
                	logData.merge(otherOne);
                	processedData.add(logData);
                } else {
                	processingData.put(logData.getId(), logData);
                }
            }
            
            sortData();
		} catch (Exception e) {
			System.out.println("Cannot read " + filename);
			e.printStackTrace();
		}
	}
	
	/** Sort and display data */
	void sortData() {
		Collections.sort(processedData);
		for(LogData l : processedData) {
			System.out.println(l);
		}
	}
	
	enum InOut { IN, OUT }
	
	/** Holds data about a log entry by parsing lines that are associated with it. */
	class LogData implements Comparable<LogData> {
		int id;
		long timestamp, timeTaken;
		String threadId, outboundTime, inboundTime, extraData, inLine, comma = "";
		InOut inOut;
				
		int getId() { return id; };
		InOut getInOut() { return inOut; }
		
		public int compareTo(LogData other) {
			Long compareTimeTaken = Long.valueOf(timeTaken);
			return compareTimeTaken.compareTo(Long.valueOf(other.getTimeTaken()));
		}
		
		/** Parses a line of data */
		void addLine(String line) {
			Matcher idMatcher = idPattern.matcher(line);
			if (idMatcher.find()) {
				id = Integer.parseInt(idMatcher.group(1));
			} else {
				Matcher dateMatcher = datePattern.matcher(line);
				
				if (dateMatcher.find()) {
					
					// look for date/time info
					Matcher timeMatcher = timePattern.matcher(line);
							
					if (timeMatcher.find()) {
						Matcher digitsMatcher = dissectTimePattern.matcher(timeMatcher.group(1));
						digitsMatcher.find();
						int hour = Integer.parseInt(digitsMatcher.group(1));
						int min = Integer.parseInt(digitsMatcher.group(2));
						int sec = Integer.parseInt(digitsMatcher.group(3));
						timestamp = (hour * 3600) + (min * 60) + sec;
					}					
					
					// what sort of line is it
					Matcher outboundMatcher = outboundPattern.matcher(line);
					if (outboundMatcher.find()) {
						outboundTime = timeMatcher.group(1);
						inOut = InOut.OUT;
					} else {
						Matcher inboundMatcher = inboundPattern.matcher(line);
						if (inboundMatcher.find()) {
							inboundTime = timeMatcher.group(1);
							inOut = InOut.IN;
							inLine = line;
						} else {
							throw new java.lang.RuntimeException("Unrecognised line " + line);
						}
					}
				} else {
					extraData += comma + line;
					comma = "\n";
				}
			}
		}
		
		long getTimestamp() { return timestamp; }
		long getTimeTaken() { return timeTaken; }
		String getOutboundTime() { return outboundTime; }
		String getInboundTime() { return inboundTime; }
		String getInLine() { return inLine; }
		
		LogData merge(LogData other) {
			if ((inOut == InOut.IN) && (other.getInOut() == InOut.OUT)) {
				timeTaken = timestamp - other.getTimestamp();
				outboundTime = other.getOutboundTime();
				return this;
			} else if ((inOut == InOut.OUT) && (other.getInOut() == InOut.IN)) {
				timeTaken = other.getTimestamp() - timestamp;
				inboundTime = other.getInboundTime();
				inLine = other.getInLine();
				return this;
			}
			
			System.out.println("Invalid situation");
			return null;
		}
		
		@Override
		public String toString() {
			if (extraData == null) { extraData = ""; }
			return " TimeTaken:" + timeTaken + " inLine: " + inLine + " " + extraData; 
		}
	}
}