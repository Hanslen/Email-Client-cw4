
public class CommandView extends AbstractCommand {

	private static final int REQ_ARGS = 2;
	int messageId;

	public CommandView(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
	}

	@Override
	public String execute() {

		InterfaceMessage msg = model.getMessage(messageId);
		if (msg != null) {
			msg.markRead(true);
			model.setholdMsg(model.getMessage(messageId));
			return model.getMessage(messageId).toString();
		} else {
			return "Error: Message does not exist";
		}
	}

	@Override
	public boolean validateArguments() {
		if (flags.length != REQ_ARGS) {
			return false;
		}

		try {
			messageId = Integer.parseInt(flags[1]);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return "Error: Cannot undo command: class CommandView";
	}
}
