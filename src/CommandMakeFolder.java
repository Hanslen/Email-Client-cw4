
public class CommandMakeFolder extends AbstractCommand {

	private static final int REQ_ARGS = 2;

	public CommandMakeFolder(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public String execute() {
		if (model.createFolder(flags[1])) {
			model.setPreviouscreatefolder(flags[1]);
			return "Success: Created folder " + flags[1];
		} else {
			return "Error: Folder " + flags[1] + " exists already.";
		}
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		if(model.deleteFolder(model.getpreviouscreatefolder().get(model.getpreviouscreatefolder().size()-1))){
			String name = model.getpreviouscreatefolder().get(model.getpreviouscreatefolder().size()-1);
			model.getpreviouscreatefolder().remove(model.getpreviouscreatefolder().size()-1);
			return "Success: tried to undo the command create folder "+name;
		}
		return "Error: tried to undo failed";
	}
}
