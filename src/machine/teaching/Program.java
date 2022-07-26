package machine.teaching;

public class Program implements Comparable<Program>{
	
	String foldedProgram;
	int librarySize;
	
	public Program(String foldedProgram, int librarySize) {
		this.foldedProgram = foldedProgram;
		this.librarySize = librarySize;
	}
	
	
	


	@Override
	public int compareTo(Program o) {
		// It is not necessary to make an extra lexicographical order
		// because programs are properly arranged this way by construction
		return Programs.getBits(this) - Programs.getBits(o);
	}





	public String getFoldedProgram() {
		return foldedProgram;
	}



	public int getLibrarySize() {
		return librarySize;
	}



}
