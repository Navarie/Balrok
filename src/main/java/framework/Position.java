package framework;

public class Position {

    private Card activeStructureCard;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    protected final int row;
    protected final int column;

    public int getRow() { return row; }
    public int getColumn() { return column; }
    public Card getActiveStructureCard() { return activeStructureCard; }

    public void setActiveStructureCard(Card card) {
        activeStructureCard = card;
    }

    public String toString() {
        return "["+ row +", "+ column +"]";
    }

    public static int distance(Position p1, Position p2) {
        int diffRow = Math.abs(p1.getRow() - p2.getRow());
        int diffColumn = Math.abs(p1.getColumn() - p2.getColumn());

        return Math.max(diffRow, diffColumn);
    }

    public boolean equals(Object o) {
        if (o == null) { return false; }
        if (o.getClass() != Position.class) { return false; }
        Position other = (Position) o;
        return row == other.row && column == other.column;
    }

    public int hashCode() {

        return (479 * row) + column;
    }
}
