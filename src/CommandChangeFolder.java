public class CommandChangeFolder extends AbstractCommand {

	private final int REQ_ARGS = 2;

	public CommandChangeFolder(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
	}

	@Override
	public String execute() {
		model.setPreviouschangefolder(model.getActiveFolderName());
		if (model.changeActiveFolder(flags[1])) {
			return "Success: Changed folder to " + flags[1];
		} else {
			return "Error: Check " + flags[1] + " exists.";
		}
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		if(model.changeActiveFolder(model.getpreviouschangefolder())){
			return "Success: tried to change active folder to "+model.getpreviouschangefolder();
		}
		return "Error: tried to undo failed";
	}
}
