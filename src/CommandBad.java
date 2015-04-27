public class CommandBad extends AbstractCommand {
	String message;
	
	public CommandBad(InterfaceClientModel model, String[] flags) throws BadCommandException {
		super(model, flags);
	}

	@Override
	public String execute() {
		return message;
	}

	public CommandBad setMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	protected boolean validateArguments() {
		return false;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return "Error: Cannot undo command: class CommandBad";
	}
}
