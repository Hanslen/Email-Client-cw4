import java.util.Iterator;


public class CommandAlias extends AbstractCommand{
	private static final int REQ_ARGS = 3;
	private static final String[] createCommand = {""};
	private static int counter = 0;
	String origin;
	String newCom;

	public CommandAlias(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		if(model.setAlias(origin, newCom)){
			model.setpreviousalias(newCom);
			return "Added alias "+newCom+" to "+origin;
		}
		
		return "Error: does not have"+origin+" or command"+newCom+"has already existed.";
		
	}

	@Override
	protected boolean validateArguments() {
		// TODO Auto-generated method stub
		if (flags.length != REQ_ARGS) {
			return false;
		}
		origin = flags[1];
		newCom = flags[2];
		return true;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		if(model.undoalias(model.getpreviousalias())){
			return "Success: tried to undo alias "+model.getpreviousalias();
		}
		return "Error: tried to undo Alias failed";
	}

}
