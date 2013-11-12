package role;

import agent.StringUtil;
import simcity.PersonAgent;

public abstract class Role
{
    protected PersonAgent person;

    protected Role()
    {
    	
    }
    
    public abstract boolean pickAndExecuteAnAction();
    
    protected void stateChanged()
    {
        person.stateChanged();
    }
    
    public void setPerson(PersonAgent p)
    {
        person = p;
    }
    
    public PersonAgent getPersonAgent() //so other agents or role players can send you Person messages.
    {
        return person;
    }
    
    protected String getName()
    {
        return StringUtil.shortName(this);
    }
    protected void Do(String msg)
    {
        print(msg, null);
    }
    protected void print(String msg)
    {
        print(msg, null);
    }
    protected void print(String msg, Throwable e)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }
} 
