
public class InterfaceRules {
	String[] rulesarg = new String[3];
	public void setarg(String destfolder, String searchtype, String searchkey){
		this.rulesarg[0] = destfolder;
		this.rulesarg[1] = searchtype;
		this.rulesarg[2] = searchkey;
	}
	
	public String[] getarg(){
		return this.rulesarg;
	}
	
}
