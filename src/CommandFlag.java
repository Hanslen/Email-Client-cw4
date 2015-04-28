
public class CommandFlag extends AbstractCommand{

	private static final int REQ_ARGS = 3;
	private int messageId;
	private String flag;
	private String successMessage;
	
	public CommandFlag(InterfaceClientModel model, String[] flags)
			throws BadCommandException {
		super(model, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		String pflag = null;
		if(model.getMessage(messageId)!= null){
			pflag = model.getMessage(messageId).getFlag();
		}
		if (model.flagMsg(messageId, flag)) {
			model.setPreviousFlag(pflag);
			return successMessage;
		} else {
			return "Error: check " + messageId + " exists.";
		}
	}

	@Override
	protected boolean validateArguments() {
		// TODO Auto-generated method stub
		if (flags.length != REQ_ARGS) {
			return false;
		}

		try {
			messageId = Integer.parseInt(flags[2]);
		} catch (NumberFormatException e) {
			return false;
		}

		if (flags[1].equalsIgnoreCase("red")) {
			flag = "red";
			successMessage = "Success: flagged " + messageId + " as "+flag;
		} else if (flags[1].equalsIgnoreCase("green")) {
			flag = "green";
			successMessage = "Success: flagged " + messageId + " as "+flag;
		} else if(flags[1].equalsIgnoreCase("blue")){
			flag = "blue";
			successMessage = "Success: flagged " + messageId + " as "+flag;
		} else if(flags[1].equalsIgnoreCase("noflag")){
			flag = "noflag";
			successMessage = "Success: flagged " + messageId + " as "+flag;
		} else {
			return false;
		}

		return true;
	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		if(model.flagMsg(messageId, model.getPreviousFlag().get(model.getPreviousFlag().size()-1))){
			String name = model.getPreviousFlag().get(model.getPreviousFlag().size()-1);
			model.getPreviousFlag().remove(model.getPreviousFlag().size()-1);
			return "Success: tried to flag "+messageId +" as "+name;
		}
		return "Error: tried to undo failed";
	}
	

}
