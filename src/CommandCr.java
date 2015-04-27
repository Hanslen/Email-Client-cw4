
public class CommandCr extends AbstractCommand{

	private static final int REQ_ARGS = 4;
	public CommandCr(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		if(model.cr(flags[1], flags[2], flags[3])){
			InterfaceRules onerule = new InterfaceRules();
			onerule.setarg(flags[1], flags[2], flags[3]);
			model.setrules(onerule);
			return "Success: Added rule and moved "+model.getcrcounter()+" messages.";
		}
		return "Error: folder does not exist";
	}

	@Override
	protected boolean validateArguments() {
		// TODO Auto-generated method stub
		if (flags.length != REQ_ARGS) {
			return false;
		}
		return true;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return "Error: Cannot undo command: class CommandCr";
	}

}
