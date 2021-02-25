import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class SortMerge {
	static boolean allFilesAreSorted = false;
	static boolean fileExist = false;
	public static void main(String[] args) {
		ArrayList<String> inputFilesNames = new ArrayList<String>();
		//default values setting
		boolean isInt = false;   
		boolean isUp = true;    
		boolean isSorted = true; 
		boolean isBig = true;
		String outputFileName = null;
		
		for(int i = 0; i < args.length; i++) {
			int length = args[i].length();
			//parameters checking 
			if(args[i].charAt(0) == '-' & length == 2) {
				switch(args[i].charAt(1)) {
				case 'i':
					isInt= true;
					break;
				case 'd':
					isUp = false;
					break;
				case 'n':
					isSorted = false;
					break;
				case 's':
					isInt = false;
					break;
				case 'a':
					isUp = true;
					break;
				case 'b':
					isBig = true;
					break;
				case 'l':
					isBig = false;
					break;
				default:
					System.out.println("Undefined parameter");
					break;
				}
			//file names adding
			}else if(length > 4){
				String end = Character.toString(args[i].charAt( length - 4)) + 
					     	 Character.toString(args[i].charAt( length - 3)) +
					    	 Character.toString(args[i].charAt( length - 2)) + 
					    	 Character.toString(args[i].charAt( length - 1));
				if(end.equals(".txt") & outputFileName == null) {
					outputFileName = args[i];
				}else if(end.equals(".txt")) {
					inputFilesNames.add(args[i]);
				}
			}
		}
		if(inputFilesNames.size() != 0) {
			if(isBig) {
				sortBigFiles(inputFilesNames, outputFileName, isInt, isUp, isSorted);
			}else {
				sortFiles(inputFilesNames, outputFileName, isInt, isUp, isSorted);
			}
		}else {
			System.out.println("There is no input or output files!");
		}
	}
	
	public static <T> void sortBigFiles(ArrayList<String> inputFilesNames, String outputFileName, boolean isInt, boolean isUp, boolean isSorted) {
		ArrayList<ArrayList<T>> filesData;
		try {
			int indexToRead = 0;
			while(!allFilesAreSorted) {
				filesData = readingBigFiles(inputFilesNames, isInt, indexToRead);
				ArrayList<T> finalList = sort(filesData, isUp);
				try {
					writeBigFile(finalList, outputFileName);
				} catch (IOException e) {
					e.printStackTrace();
				}
				indexToRead++;
				if(inputFilesNames.size() < 1) {
					allFilesAreSorted = true;
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public static <T> void writeBigFile(ArrayList<T> finalList, String outputFileName) throws IOException {
		File file = new File(outputFileName);
		FileWriter fr;
		if(fileExist) {
			fr = new FileWriter(file, true);
		}else {
			fr = new FileWriter(file);
		}
		BufferedWriter bw = new BufferedWriter(fr);
		for(int i = 0; i < finalList.size(); i++) {
			String text = (String) finalList.get(i);
			bw.newLine();
			bw.write(text);	
		}
		bw.close();
		System.out.println("File has been wrote!");
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<ArrayList<T>> readingBigFiles(ArrayList<String> inputFilesNames, boolean isInt, int indexToRead) throws FileNotFoundException {
		ArrayList<ArrayList<T>> result = new ArrayList<ArrayList<T>>();
		
		for(int i = 0; i < inputFilesNames.size(); i++) {
			    File file=new File(inputFilesNames.get(i) );
				ArrayList<T> arrList = new ArrayList<T>();
				Scanner sc = new Scanner(file);
				while(indexToRead > 1) {
					sc.nextLine();
					indexToRead --;
				}
				
				if(isInt) {
					try {
						arrList.add( (T) (Integer.valueOf(sc.nextLine())) );
					} catch (NumberFormatException e) {
						e.printStackTrace();
						System.out.println("File format error");
					} catch (ClassCastException e) {
				    	e.printStackTrace();
				    }
				}else {
					try {
						arrList.add( (T) sc.nextLine() );
					} catch (ClassCastException e) {
						e.printStackTrace();
					}
				}
				if(!sc.hasNext()) {
					inputFilesNames.remove(i);
				}
				
				sc.close();
				result.add(arrList);
		}
		
		return result;
	}
	
	public static <T> void sortFiles(ArrayList<String> inputFilesNames, String outputFileName, boolean isInt, boolean isUp, boolean isSorted) {
		ArrayList<ArrayList<T>> filesData;
		try {
			filesData = readingFiles(inputFilesNames, isInt);
			//sorting of original files if they are not sorted
			if(!isSorted) {
				filesData = sortOriginalFiles(filesData, isInt, isUp);
			}
			//sorting of files between themselves
			ArrayList<T> finalList = sort(filesData, isUp);
			writeFile(finalList, outputFileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public static <T> void writeFile(ArrayList<T> finalList, String outputFileName) throws FileNotFoundException {
		File file = new File(outputFileName);
		PrintWriter pw = new PrintWriter(file);
		for(int i = 0; i < finalList.size(); i++) {
			pw.println(finalList.get(i));	
		}
		pw.close();
		System.out.println("File has been wrote!");
	}
	
	public static <T> ArrayList<ArrayList<T>> sortOriginalFiles(ArrayList<ArrayList<T>> filesData, boolean isInt, boolean isUp){
		for(int i = 0; i < filesData.size(); i++) {
			filesData.set(i, sortOriginal( filesData.get(i), 0, filesData.get(i).size()-1 , isUp));
		}
		return filesData;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<ArrayList<T>> readingFiles(ArrayList<String> inputFilesNames, boolean isInt) throws FileNotFoundException {
		ArrayList<ArrayList<T>> result = new ArrayList<ArrayList<T>>();
		
		for(int i = 0; i < inputFilesNames.size(); i++) {
			    File file=new File(inputFilesNames.get(i) );
				ArrayList<T> arrList = new ArrayList<T>();
				Scanner sc = new Scanner(file);
				
				while(sc.hasNextLine()) {
					if(isInt) {
						try {
							arrList.add( (T) (Integer.valueOf(sc.nextLine())) );
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("File format error");
						} catch (ClassCastException e) {
					    	e.printStackTrace();
					    }
					}else {
						try {
							arrList.add( (T) sc.nextLine() );
						} catch (ClassCastException e) {
							e.printStackTrace();
						}
					}
				}
				sc.close();
				result.add(arrList);
		}
		
		return result;
	}
	
	public static <T> ArrayList<T> sort( ArrayList<ArrayList<T>> filesData, boolean isItUp) {
		ArrayList<T> returnedArrList;
		
		int nextInd = filesData.size()/2;
		if(nextInd > 0) {
			ArrayList<ArrayList<T>> devidedArr = new ArrayList<ArrayList<T>>();
			for(int i = 0; i < nextInd; i++) {
				devidedArr.add(filesData.get(i));
			}
			ArrayList<T> firstArr = sort(devidedArr, isItUp);
			devidedArr.clear();
			for(int i = nextInd; i < filesData.size(); i++) {
				devidedArr.add(filesData.get(i));
			}
			ArrayList<T> secondArr = sort(devidedArr, isItUp);
			
			returnedArrList = sortTwoFiles(firstArr, secondArr, isItUp);
		}else {
			returnedArrList = filesData.get(0);
		}
		return returnedArrList;
	}
	
	public static <T> ArrayList<T> sortTwoFiles(ArrayList<T> firstArr, ArrayList<T> secondArr, boolean isUp) {
		int bufferLength = firstArr.size() + secondArr.size();
		ArrayList<T> arr = new ArrayList<T>();
		int firstInd = 0;
		int secondInd = 0;
		for(int i = 0; i < bufferLength; i++) {
			if(firstInd >= firstArr.size()) {
				arr.add(secondArr.get(secondInd));
				secondInd++;
			}else if(secondInd >= secondArr.size()) {
				arr.add(firstArr.get(firstInd));
				firstInd++;
				
			}else if(compare(firstArr.get(firstInd), secondArr.get(secondInd), isUp) ) {
				arr.add(firstArr.get(firstInd));
				firstInd++;
			}else {
				arr.add(secondArr.get(secondInd));
				secondInd++;
			}
		}
		return arr;
	}
	

	public static <T> ArrayList<T> sortOriginal( ArrayList<T> array, int leftInd, int rightInd, boolean isUp) {
		int nextInd = leftInd + (rightInd - leftInd)/2 + 1;
		
		if(rightInd > leftInd + 1) {
			array = sortOriginal(array, leftInd, nextInd -1, isUp);
			array = sortOriginal(array, nextInd, rightInd, isUp);
		}
		ArrayList<T> buffer = new ArrayList<T>();
		int cursor = leftInd;
		int middle = nextInd;
		
		try {
			for(int i = 0; i <= rightInd - leftInd; i++) {
				if(cursor < middle & (nextInd > rightInd || compare(array.get(cursor), array.get(nextInd), isUp) ) ) {
					buffer.add( array.get(cursor));
					cursor++;
				}else {
					buffer.add(array.get(nextInd));
					nextInd++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		
		array = arrayCopy(buffer, array, leftInd, rightInd);
		return array;
	}
	
	public static <T> ArrayList<T> arrayCopy(ArrayList<T> buffer, ArrayList<T> array, int leftInd, int rightInd){
		int j =0;
		for(int i = leftInd; i <= rightInd; i++) {
			array.set(i, buffer.get(j));
			j++;
		}
		return array;
	}
	
	public static <T> boolean compare(T data1, T data2, boolean isUp) {
		try {
			if(data1 instanceof String) {
				return ( ( (String)data1 ).compareTo( (String) data2 ) < 1) ? isUp : !isUp;  
			}else {
				return ( (int) data1 < (int) data2 ) ? isUp : !isUp;
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return false;
		}
	}

}
