package ss.Scrabble.Test;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.Scrabble.Game.Board;
import ss.Scrabble.Game.Cell;
import ss.Scrabble.Game.Tile;
import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

public class TestBoard {
    Board board;
    List<Tile> tiles;
    String word;


    @BeforeEach
    public void setup(){
        board = new Board();
        board.reset();
        tiles = new ArrayList<Tile>();
        tiles.add(new Tile('W', 4));
        tiles.add(new Tile('O', 1));
        tiles.add(new Tile('R', 1));
        tiles.add(new Tile('D', 2));
        word = "word";
    }

    @Test
    public void testBoardSize(){
        int size = board.getCells().length * board.getCells()[0].length;
        assertEquals(225, size);
    }

    @Test
    public void testPremiumFields(){
        Assert.assertEquals(Cell.CellValue.DOUBLE_WORD, board.getField(7,7).getCellValue());
        assertEquals(Cell.CellValue.DOUBLE_WORD, board.getField(11,3).getCellValue());
        assertEquals(Cell.CellValue.DOUBLE_WORD, board.getField(4,10).getCellValue());
        assertEquals(Cell.CellValue.TRIPLE_WORD, board.getField(0,7).getCellValue());
        assertEquals(Cell.CellValue.TRIPLE_WORD, board.getField(7,14).getCellValue());
        assertEquals(Cell.CellValue.DOUBLE_LETTER, board.getField(3,0).getCellValue());
        assertEquals(Cell.CellValue.DOUBLE_LETTER, board.getField(12,8).getCellValue());
        assertEquals(Cell.CellValue.TRIPLE_LETTER, board.getField(5,5).getCellValue());
        assertEquals(Cell.CellValue.TRIPLE_LETTER, board.getField(9,1).getCellValue());
    }

    @Test
    public void testFields(){
        assertTrue(board.isField(5,7));
        assertTrue(board.isField(14,14));
        assertFalse(board.isField(21, 8));
    }

    @Test
    public void testEmptyDeepCopy() {
        Board copy = board.deepCopy();
        for (int i = 0; i < copy.getCells().length; i++) {
            for (int j = 0; j < copy.getCells()[0].length; j++) {
                assertTrue(board.getCells()[i][j].getLetter() == copy.getCells()[i][j].getLetter());
            }
        }
    }

    @Test
    public void testDeepCopy() {
        board.setWordHorizontal(5,7, word);
        Board copy = board.deepCopy();
        for (int i = 0; i < copy.getCells().length; i++) {
            for (int j = 0; j < copy.getCells()[0].length; j++) {
                assertTrue(board.getCells()[i][j].getLetter() == copy.getCells()[i][j].getLetter());
            }
        }
    }

    @Test
    public void testEmptyBoard(){
        assertTrue(board.isBoardEmpty());
        board.setWordHorizontal(2,3,word);
        assertFalse(board.isBoardEmpty());
    }


    @Test
    public void testHasWordSpaceHorizontal(){
        assertTrue(board.isValidWordSpaceHorizontal(7,7,word));
        board.setWordHorizontal(3,4, word);
        assertFalse(board.isValidWordSpaceHorizontal(3,5, word));
    }

    @Test
    public void testHasWordSpaceVertical(){
        assertTrue(board.isValidWordSpaceVertical(7,7,word));
        board.setWordVertical(3,4, word);
        assertFalse(board.isValidWordSpaceVertical(4,4, word));
    }

    @Test
    public void testWordsOnBoard(){
        List<String> expected = new ArrayList<>();
        expected.add("YARD");
        expected.add("YARDS");
        board.setWordHorizontal(7,7, "YARD");
        board.setWordVertical(6,8, "YARDS");
        assertEquals(expected, board.getWordsOnBoard());
    }

    @Test
    public void testLettersToPlay(){
        String expected = "YRDS";
        board.setWordHorizontal(7,7, "YARD");
        assertEquals(expected, board.getLettersToPlayVertical(6,8, "YARDS"));
    }


}
