package projectmp.common.inventory;

import projectmp.common.inventory.itemstack.ItemStack;
import projectmp.common.item.ItemChessPiece;


public class InventoryChessboard extends Inventory{

	public InventoryChessboard(){
		super();
	}
	
	public InventoryChessboard(String id, int x, int y){
		super(id, x, y);
		setMaxCapacity(64);
		initSlots();
	}
	
	private int getIndexFromXY(int x, int y){
		return ((y * 8) + x);
	}
	
	@Override
	public void initSlots(){
		super.initSlots();

		// pawns
		for (int x = 0; x < 8; x++) {
			setSlot(getIndexFromXY(x, 1), new ItemStack("chessBlack", ItemChessPiece.PAWN));
		}
		
		for (int x = 0; x < 8; x++) {
			setSlot(getIndexFromXY(x, 6), new ItemStack("chessWhite", ItemChessPiece.PAWN));
		}
		
		// black starting at top
		setSlot(getIndexFromXY(0, 0), new ItemStack("chessBlack", ItemChessPiece.ROOK));
		setSlot(getIndexFromXY(1, 0), new ItemStack("chessBlack", ItemChessPiece.KNIGHT));
		setSlot(getIndexFromXY(2, 0), new ItemStack("chessBlack", ItemChessPiece.BISHOP));
		setSlot(getIndexFromXY(3, 0), new ItemStack("chessBlack", ItemChessPiece.QUEEN));
		setSlot(getIndexFromXY(4, 0), new ItemStack("chessBlack", ItemChessPiece.KING));
		setSlot(getIndexFromXY(5, 0), new ItemStack("chessBlack", ItemChessPiece.BISHOP));
		setSlot(getIndexFromXY(6, 0), new ItemStack("chessBlack", ItemChessPiece.KNIGHT));
		setSlot(getIndexFromXY(7, 0), new ItemStack("chessBlack", ItemChessPiece.ROOK));
		
		// white
		setSlot(getIndexFromXY(0, 7), new ItemStack("chessWhite", ItemChessPiece.ROOK));
		setSlot(getIndexFromXY(1, 7), new ItemStack("chessWhite", ItemChessPiece.KNIGHT));
		setSlot(getIndexFromXY(2, 7), new ItemStack("chessWhite", ItemChessPiece.BISHOP));
		setSlot(getIndexFromXY(3, 7), new ItemStack("chessWhite", ItemChessPiece.QUEEN));
		setSlot(getIndexFromXY(4, 7), new ItemStack("chessWhite", ItemChessPiece.KING));
		setSlot(getIndexFromXY(5, 7), new ItemStack("chessWhite", ItemChessPiece.BISHOP));
		setSlot(getIndexFromXY(6, 7), new ItemStack("chessWhite", ItemChessPiece.KNIGHT));
		setSlot(getIndexFromXY(7, 7), new ItemStack("chessWhite", ItemChessPiece.ROOK));

	}

}
