
public class exp{
	private String descp,cat;
	private int amt,id;
	private String date;
	public exp(int id,String descp,String cat,String date,int amt) {
		this.id=id;
		this.descp=descp;
		this.cat=cat;
		this.date=date;
		this.amt=amt;
	}
	public int getid() {
		return id;
	}
	public String getdescp() {
		return descp;
	}
	public String getcat() {
		return cat;
	}
	public String getdate() {
		return date;
	}
	public int getamt() {
		return amt;
	}
}
