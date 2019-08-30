/** *************************************
 * @desc Chess board to store FEN chess string
 * @author Pattreeya Tanisaro
 * 
 *************************************** */
class Board
{	 
	private char[][] board;
	final int N = 8;
		
	Board( String fenString )
	{
		
		board = new char[N][N];
		
		fenStringToArray(fenString);
	}
	
	/**
	 * Convert fenString to board pattern.
	 * @param fenString
	 */
	void fenStringToArray(String fenString)
	{
		
        final String[] parts = fenString.split("\\s+");
        final String[] rows = parts[0].split("/");
        
        for (int i = N-1; i >= 0; i--) { // row
        	String row = rows[N-i-1];
        	int l=0; // counter of column
            for (int j = 0; j < row.length(); j++) { // column
              final char c = row.charAt(j);
              if (c >= '1' && c <= '8') {
            	  int num_empty = c - '0';
            	  for (int k = 0; k < num_empty; k++) 
            		  board[i][l++] = 0;
              	}
              else { 
            	  board[i][l++] = c;
              }
            }
          }
	}
	
	/**
	 * Convert board position to fenString
	 */
	String getFenString() 
	{
		String fenString="";
		
		for( int i=N-1; i>=0; i--) { // row
			for( int j=0; j<N; j++) {
				if (board[i][j] == 0) {
					int nEmpty=0;
					while( (j<N) && (board[i][j] == 0) ) {
						nEmpty += 1;
						j++;
					}
					fenString += nEmpty;
					j--;
				}
				else {
					fenString += board[i][j];
				}	
			} // end column
			if (i != 0 ) // we don't append slash at the last row
				fenString += "/";
		}
		
		return fenString;
	}
    
	/**
	 * Display the chess board 
	 */
    void displayBoard()
    {

    	for(int i=N-1; i>=0; i--) {
    		String msg="";
    		for(int j=0; j<N; j++) {
    			if (board[i][j] == 0) {
    				msg +='.';    		
    			}
    			else {
    				msg += board[i][j];
    			}
    		}
    		System.out.println(msg+"\n");
    	}
    }
    
    /**
     * Display FEN String
     */
    void displayFenString(String fenString) 
    {
    	System.out.println( fenString );	
    }
    
    /**
     * Validate the given position in "a..h" and "1..8"
     * @param pos as char[]
     * @return false if the given position is invalid.
     */
    boolean validPos( char[] Pos) 
    {
    	boolean ok = true;
    	char col=Pos[0]; char row=Pos[1];
    	ok = ok && ("abcdefgh".indexOf(col) != -1);
    	ok = ok && ("12345678".indexOf(row) != -1);
    	
    	return ok;
    }
    
    int columnIdx(char c) 
    {
    	return "abcdefgh".indexOf( c );	
    }
    
    int rowIdx(char r)
    {
    	return "12345678".indexOf( r );
    }
    
    
    /**    
     * Move element from current position to new position
     * @return true if the moving to new position is successful, false otherwise.
     */
    boolean move(String _fromPos, String _toPos)
    {
    	
    	char[] fromPos = _fromPos.toCharArray();
    	char[] toPos = _toPos.toCharArray();
    	if ( !validPos( fromPos)  || !validPos(toPos) ) {
    		System.out.println("Error: Invalid position!");
    		return false;
    	}
    	
    	// Columns = a...h and Rows = 1...8 
    	int fromC = columnIdx(fromPos[0]); int fromR = rowIdx( fromPos[1] );
    	int toC = columnIdx(toPos[0]);     int toR = rowIdx( toPos[1] );
    	
    	// in order to be able to move successfully, we check simple the following conditions
    	// 1. fromPos contain element
    	// 2. ....
    	char fromVal = board[fromR][fromC];
    	char toVal = board[toR][toC];
    	// if both positions make sense, we update the board position and return true.
    	if( (fromVal != 0)  ) {
    		board[fromR][fromC] = 0;
    		board[toR][toC] = fromVal;
    		System.out.println("Moving "+ fromVal+ " at "+_fromPos +" to "+_toPos+".");
    		return true;
    	}
    	System.out.println("Error: Cannot move to new position!");
        return false;
    	
    	
    }
         
} // **************** end Board class  *******************


/**
 * Chess main class
 * 
 */
public class Chess
{
	/**
	 *  Check if the given fenString is a valid string.
	 *  - a string contains 8 groups splitting by slash
	 *  - each group contains 8 char
	 * @param fenString
	 * @return
	 */
	static boolean validFenString(String fenString) 
	{
		final int N = 8;
		final String[] parts = fenString.split("\\s+");
        final String[] rows = parts[0].split("/");
     
        for (int i= 0; i <N; i++) { // row
        	char[] row = rows[i].toCharArray();
        	// check whether each char is valid
        	// check whether in each row has the sum of 8
        	int sum=0;
        	for (int j=0; j<row.length; j++) {
        		int idx ="rqkbnpRQKBNP12345678".indexOf( row[j] );
        		if ( idx == -1 ) {
        			System.out.println("Error: at row: "+(i+1)+ ", " +rows[i]+" invalid string!");
        			return false;
        		}
        		if ( Character.isDigit(row[j])  ) {
        			sum += Character.getNumericValue( row[j] );
        		}
        		else {
        			sum += 1;
        		}
        	}
        	if (sum != N) {
        		System.out.println("Error: at row [ "+ (i+1) +"] of "+rows[i]);
        		return false;
        	}
        }
		return true;
	}
	
	public static void main (String[] args) 
	{
		boolean ok;
		String initFen="rbnqk2r/ppp3pp/8/8/8/8/2P2PPP/RBNQKR1B";
		if ( ! Chess.validFenString( initFen) ) {
			System.out.println( "The given string: "+initFen+" is not a valid FEN string!" );
			
		}
		else {
			System.out.println( "Init: "+initFen );
			Board board = new Board( initFen ); 
			board.displayBoard();
			ok = board.move("c2", "c4");
			board.displayBoard();
			String strFen2 = board.getFenString();
			board.displayFenString(strFen2);
			ok = board.move("e1", "e5");
			board.displayBoard();
		}
		
		String initFen2 = "rbnqk2r/ppp3pp/8/8/8/8/2P2PPP/RBNQKR1z"; // 7/<--create error 
		if ( ! Chess.validFenString( initFen2) ) {
			System.out.println( "The given string: "+initFen2+" is not a valid FEN string!" );
			
		}
		else {
			Board board2 = new Board( initFen2 ); 
			board2.displayBoard();
		}
		
		String initFen3 = "rbnqkbrn/pppppppp/8/8/8/8/PPPPPPPP/RBNQKRBB";  
		if ( ! Chess.validFenString( initFen3) ) {
			System.out.println( "The given string: "+initFen3+" is not a valid FEN string!" );
			
		}
		else {
			System.out.println( "Init: "+initFen3 );
			Board board3 = new Board( initFen3 ); 
			board3.displayBoard();
			ok = board3.move("h2", "h4");
			ok = board3.move("a2", "a4");
			ok = board3.move("b2", "b3");
			ok = board3.move("c7", "c5");
			ok = board3.move("c8", "c7");
			board3.displayBoard();
			String fenString4 = board3.getFenString();
			board3.displayFenString(fenString4);
		}
		
	}

	
}
