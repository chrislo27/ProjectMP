package projectmp.common.inventory.gui;


public class TestGui extends GuiHandler{

	public TestGui(){
		super();
		
		slots.add(new Slot(0, 0, 64, 64));
		slots.add(new Slot(0, 0, 128, 64));
	}
	
}
