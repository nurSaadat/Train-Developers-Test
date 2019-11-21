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
        singletons.add(new MySQLConnector(3306, "RailwayStation", "user", "Password123!"));
        singletons.add(new AuthenticationFilter());
        singletons.add(new WorkerAuthenticationFilter());
        singletons.add(new AgentAuthenticationFilter());
        singletons.add(new ManagerAuthenticationFilter());
        singletons.add(new LogInService());
        singletons.add(new LogOutService());
        singletons.add(new SignUpService());
        singletons.add(new ListTicketsService());
        singletons.add(new CheckLogInService());
        singletons.add(new ProfileInfoService());
        singletons.add(new ChangePasswordService());
        singletons.add(new PaymentService());
        singletons.add(new ListSeatsService());
        singletons.add(new BookOrdersService());
        singletons.add(new BookTicketService());
        singletons.add(new BookPassengersService());
        singletons.add(new ListAgentsService());
        singletons.add(new WorkerProfileInfoService());
        singletons.add(new CancelTicketService());
        singletons.add(new AddManagerService());
        singletons.add(new ListSubordinateEmployeeService());
        singletons.add(new ShowRecentNewsService());
        singletons.add(new ShowAllNewsService());
        singletons.add(new AddNewsService());
        singletons.add(new AddScheduleService());
        singletons.add(new ListSchedulesService());
        singletons.add(new AddTrainService());
        singletons.add(new AddCarriageService());
        singletons.add(new ListTrainsService());
        singletons.add(new AddTrainScheduleService());
        singletons.add(new LogFilter());
        singletons.add(new ListLogsService());
        singletons.add(new EditTicketService());
        singletons.add(new ShowAllPaychecksService());
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
