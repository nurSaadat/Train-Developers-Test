package backend;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class MainApplication extends Application {
    
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();
    
    public MainApplication() throws SQLException, ClassNotFoundException {
        
        singletons.add(new ListRoutesService());
        singletons.add(new MySQLConnector(3306, "mydb", "user", "8264"));
        
    }
    
    @Override
    public Set<Class<?>> getClasses() {
        
        return empty;
        
    }

    @Override
    public Set<Object> getSingletons() {
        
        return singletons;
        
    }
    
}
