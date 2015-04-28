public class CommandRename extends AbstractCommand {

	private static final int REQ_ARGS = 3;

	public CommandRename(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
	}

	@Override
	public String execute() {
		String oldName = flags[1];
		String newName = flags[2];
		if (model.renameFolder(oldName, newName)) {
			model.setPreviousRename(oldName, newName);
			return "Success: renamed " + oldName + " to " + newName;
		} else {
			if(oldName.equals("red")||oldName.equals("green")||oldName.equals("blue")){
				return "Cannot rename smart folder";
			}
			return "Error: " + oldName + " does not exist or " + newName
					+ " already exists.";
		}
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		if(model.renameFolder(model.getPreviousRename().get(model.getPreviousRename().size()-1)[1],model.getPreviousRename().get(model.getPreviousRename().size()-1)[0])){
			String name1 = model.getPreviousRename().get(model.getPreviousRename().size()-1)[1];
			String name0 = model.getPreviousRename().get(model.getPreviousRename().size()-1)[0];
			model.getPreviousRename().remove(model.getPreviousRename().size()-1);
			return "Success: tried to rename "+name1 +" to "+name0;
		}
		return "Error: tried to undo failed";
	}
}
