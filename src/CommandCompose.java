
public class CommandCompose extends AbstractCommand {
	private View view;

	InterfaceMessage msg;
	private static final int REQ_ARGS = 1;

	public CommandCompose(InterfaceClientModel model, View view, String[] commandInput)
			throws BadCommandException {
		super(model, commandInput);
		this.view = view;
	}

	@Override
	public String execute() {
		view.compose(this);
		if (model.sendMessage(msg)) {
			return "Success: sent";
		} else {
			return "Failed: Could not send.";
		}
	}

	public void setMessage(Message msg) {
		this.msg = msg;
	}

	@Override
	public boolean validateArguments() {
		return flags.length == REQ_ARGS;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return "Error: Cannot undo command: class CommandCompose";
	}

}
