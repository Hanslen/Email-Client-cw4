
public class CommandMoveMessage extends AbstractCommand {

	private static final int REQ_ARGS = 3;
	private int messageId;

	public CommandMoveMessage(InterfaceClientModel model, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
	}

	@Override
	public boolean validateArguments() {
		if (flags.length != REQ_ARGS) {
			return false;
		}

		try {
			messageId = Integer.parseInt(flags[1]);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	@Override
	public String execute() {
		model.setpreviousmovemsgfolder(model.getActiveFolderName(), flags[2]);
		if(model.getActiveFolderName().equals("red")||model.getActiveFolderName().equals("green")||model.getActiveFolderName().equals("blue")||flags[2].equals("red")||flags[2].equals("green")||flags[2].equals("blue")){
			return "Error: Check message "+messageId+" exists and that inbox is a folder, and that neither the current folder or target folder are smart folders.";
		}
		if (model.move(messageId, flags[2])) {
			return "Success: Moved " + messageId + " to " + flags[2];
		} else {
			return "Error: Check message " + messageId + " exists and that "
					+ flags[2] + " is a folder.";
		}
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		String temple = model.getActiveFolderName();
		model.changeActiveFolder(model.getpreviousmovemsgfolder().get(model.getpreviousmovemsgfolder().size()-1)[1]);
		if(model.move(messageId, model.getpreviousmovemsgfolder().get(model.getpreviousmovemsgfolder().size()-1)[0])){
			model.changeActiveFolder(temple);
			String name = model.getpreviousmovemsgfolder().get(model.getpreviousmovemsgfolder().size()-1)[0];
			model.getpreviousmovemsgfolder().remove(model.getpreviousmovemsgfolder().size()-1);
			return "Success: tried to move message "+messageId +" back to "+name;
		}
		return "Error: tried to undo move failed";
	}
}
