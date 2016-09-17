import java.io.*;
import java.util.*;

public class SecureSystem {
	/* Subject to value mapping */
	static Map<String, Integer> subjToVal;
	
	public static void main(String[] args) throws FileNotFoundException {
		FileReader curFile;
		BufferedReader curData;
		String lineData;
		int holder;
		
		init();
		
		// Parse through file and perform various operations
		if(args.length > 0) {
			File file = new File(args[0]);
			System.out.println("Reading from file: " + file.getName());
			System.out.println();
			try {
				curFile = new FileReader(file);
				curData = new BufferedReader(curFile);
				
				while((lineData = curData.readLine()) != null){
					InstructionObject current = 
							new InstructionObject(lineData);
					if(current.instrType.equals("bad")){
						BadInstruction badInst = new BadInstruction();
						ReferenceMonitor.doBad(badInst);
						continue;
					}
					holder = ReferenceMonitor.execute(current);
					if(current.instrType.equals("read")) {
						subjToVal.replace(current.subject, holder);
						System.out.println(current.subject + " reads " + 
								current.object);
						printStatus();
					}
					if(current.instrType.equals("write")) {
						System.out.println(current.subject + " writes value " +
								current.value + " to " + current.object);
						printStatus();
					}
				}
				curData.close();
				curFile.close();
			}
			catch (IOException e){
				System.out.println("Cannot find file.");
			}
		}
		else
			throw new FileNotFoundException("Need a file to parse!");
	}
	
	/* Initializes managers and creates subjects and objects */
	private static void init(){
		subjToVal = new HashMap<String, Integer>();
		ReferenceMonitor.ObjectManager.createObjectManager();
		ReferenceMonitor.createReferenceMonitor();
		ReferenceMonitor.SecurityLevel low = 
				ReferenceMonitor.SecurityLevel.LOW;
		ReferenceMonitor.SecurityLevel high = 
				ReferenceMonitor.SecurityLevel.HIGH;
		
		// Creation of subjects
		subjToVal.put("lyle", 0);
		ReferenceMonitor.subjToLabel.put("lyle", low);
		subjToVal.put("hal", 0);
		ReferenceMonitor.subjToLabel.put("hal", high);
		
		//Creation of objects
		ReferenceMonitor.ObjectManager.objToVal.put("lobj", 0);
		ReferenceMonitor.objToLabel.put("lobj", low);
		ReferenceMonitor.ObjectManager.objToVal.put("hobj", 0);
		ReferenceMonitor.objToLabel.put("hobj", high);
	}
	
	/* Prints out the current operation and current status of subjects
	 * and objects */
	private static void printStatus() {
		System.out.println("The current state is:");
		System.out.println("   lobj has value: "  + 
				ReferenceMonitor.ObjectManager.objToVal.get("lobj"));
		System.out.println("   hobj has value: " + 
				ReferenceMonitor.ObjectManager.objToVal.get("hobj"));
		System.out.println("   lyle has recently read: " + 
				subjToVal.get("lyle"));
		System.out.println("   hal has recently read: " + 
				subjToVal.get("hal"));
		System.out.println();
	}
	
	/* Manages the security levels and allows or denies operations from
	 * subjects to objects */
	private static class ReferenceMonitor {
		/* Subject to label mapping */
		static Map<String, SecurityLevel> subjToLabel;
		
		/* Object to label mapping */
		static Map<String, SecurityLevel> objToLabel; 
		
		/* Initializes the security level mappings */
		private static void createReferenceMonitor() {
			subjToLabel = new HashMap<String, SecurityLevel>();
			objToLabel = new HashMap<String, SecurityLevel>();
		}
		
		/* Possible security levels */
		private enum SecurityLevel {
			HIGH, LOW
		}
		
		/* Determines whether to execute requested instruction */
		private static int execute(InstructionObject current) {
			String curSub = current.subject;
			String curObj = current.object;
			
			boolean execute = compareLabels(current);
			if(execute && current.instrType.equals("read"))
				return ObjectManager.read(curSub, curObj);
			else if(execute && current.instrType.equals("write"))
				return ObjectManager.write(curSub, curObj, current.value);	
			return 0;
		}
		
		/* Executes a BadInstruction by letting the user know it was an illegal
		 * or incorrect instruction */
		private static void doBad(BadInstruction badInst) {
			System.out.println(badInst.value);
			printStatus();
		}
		
		/* Compares subject security level to object security level */
		private static boolean compareLabels(InstructionObject current) {
			SecurityLevel cur = subjToLabel.get(current.subject);
			SecurityLevel other = objToLabel.get(current.object);
			
			if(current.instrType.equals("read") && 
					(cur.equals(SecurityLevel.HIGH) && 
							other.equals(SecurityLevel.HIGH) || 
					 cur.equals(SecurityLevel.HIGH) && 
			   		   		other.equals(SecurityLevel.LOW) || 
			   		 cur.equals(SecurityLevel.LOW) && 
			   		   		other.equals(SecurityLevel.LOW)))
				return true;
			else if(current.instrType.equals("write") && 
					(cur.equals(SecurityLevel.HIGH) && 
							other.equals(SecurityLevel.HIGH) || 
					 cur.equals(SecurityLevel.LOW) && 
					 		other.equals(SecurityLevel.HIGH) || 
					 cur.equals(SecurityLevel.LOW) && 
					 		other.equals(SecurityLevel.LOW)))
				return true;
			else
				return false;
		}
		
		/* Object manager nested for protection by ReferenceMonitor */
		private static class ObjectManager {
			/* Object to value mapping */
			static Map<String, Integer> objToVal;
			
			/* Initializes object mapping to values */
			private static void createObjectManager() {
				objToVal = new HashMap<String, Integer>();
			}
			
			/* Reads the current value of the desired object */
			public static int read(String curSub, String curObj) {
				return objToVal.get(curObj);
			}
			
			/* Writes val to the desired object */
			public static int write(String curSub, String curObj, int val) {
				objToVal.replace(curObj, val);
				return 1;
			}
		}
	}
	
	/* InstructionObject that holds parsed line data */
	private static class InstructionObject {
		String instrType;
		String subject;
		String object;
		int value;
		
		/* Creates a InstructionObject object */
		private InstructionObject(String data) {
			Scanner lineScanner = new Scanner(data);
			instrType = lineScanner.next().toLowerCase();
			if(instrType.equals("write") || instrType.equals("read")){
				if(lineScanner.hasNext()){
					subject = lineScanner.next().toLowerCase();
					if(!subjToVal.containsKey(subject)) 
						instrType = "bad";
					if(lineScanner.hasNext()){
						object = lineScanner.next().toLowerCase();
						if(!lineScanner.hasNext() && instrType.equals("write")
						   || !ReferenceMonitor.ObjectManager.
						   		objToVal.containsKey(object))
							instrType = "bad";
						if(lineScanner.hasNext() && instrType.equals("write")){
							value = Integer.parseInt(lineScanner.next());
							if(lineScanner.hasNext())
								instrType = "bad";
						}
					}
					else
						instrType = "bad";
				}
				else
					instrType = "bad";
			}
			else
				instrType = "bad";
			lineScanner.close();
		}
	}
	
	/* Object used when an illegal or incorrect instruction is made */
	private static class BadInstruction {
		String value; 
		
		/* Creates a BadInstruction object */
		private BadInstruction() {
			value = "Bad Instruction";
		}
	}
	
}
