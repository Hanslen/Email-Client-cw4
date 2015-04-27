import java.util.ArrayList;

import emailConnector.InterfaceConnector;
import emailConnector.StandardConnector;

public class Controller {
	private InterfaceClientModel model;
	private CommandFactory commandFactory;
	private View view;
	private InterfaceConnector connector;
	
	ArrayList<String> stackCommand = new ArrayList<String>();
	ArrayList<String> undoCommand = new ArrayList<String>();
	int nowCommand = 0;
	int undotimes = 1;
	int nowredocomamnd = 0;
	int redotimes = 1;
	
	public Controller() {
		view = new View(this);
		connector = StandardConnector.getInstance();
		model = new ClientModel(connector);
		commandFactory = CommandFactory.getInstance();
		commandFactory.setReferences(model, view);
	}

	public void begin() {
		view.getInput();
	}

	public String processInput(String theInput) {
		String response = "";
		//AbstractCommand command;
		int undo = 0;
		int redo = 0;

		try {
			if(theInput.length() == 4){
				if(theInput.substring(0, 4).equals("undo")){
					nowCommand = stackCommand.size();
					if(nowCommand != 0){
						response = commandFactory.buildCommand(stackCommand.get(nowCommand-undotimes)).undo();
						undoCommand.add(stackCommand.get(nowCommand-undotimes));
						if(response.substring(0, 5).equals("Error")){
							undotimes++;
						}
					}
					else{
						response = "Error: No command to undo";
					}
					undo = 1;
				}
				else if(theInput.substring(0, 4).equals("redo")){
					nowredocomamnd = undoCommand.size();
					if(nowredocomamnd != 0){
						response = commandFactory.buildCommand(undoCommand.get(nowredocomamnd-redotimes)).execute();
						redotimes++;
					}
					else{
						response = "Error: No command to redo";
					}
					redo = 1;
				}
			}
			if(undo == 0 && redo == 0){
				response = commandFactory.buildCommand(theInput).execute();
				undotimes = 1;
			}
			
			if(!response.substring(0, 5).equals("Error")){
				stackCommand.add(theInput);
			}
			undo = 0;
			redo = 0;
			
		} catch (BadCommandException bce) {
			response = bce.getMessage();
		}
		return response;
	}
}
