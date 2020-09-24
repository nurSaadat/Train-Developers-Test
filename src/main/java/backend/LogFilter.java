package backend;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class LogFilter implements Filter {

	private ServletContext context;

    public void init(FilterConfig fConfig) throws ServletException {
    	
        this.context = fConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    	if(!ListLogsService.isLog())
    		chain.doFilter(request, response);
    	else {
    	
    		HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            HttpSession session = req.getSession(false);
            
            FileWriter out = new FileWriter("Project_log/server.log", true);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            if (session == null) {   //checking whether the session exists
            	
                out.write(formatter.format(date) + " - Method: " + req.getMethod() + ", Request URL: " + req.getRequestURI() + ", User: not set, IP: " + req.getRemoteAddr() + ".\n");
                out.close();
                
                chain.doFilter(request, response);
            } else {
            	
            	out.write(formatter.format(date) + " - Method: " + req.getMethod() + ", Request URL: " + req.getRequestURI() + ", User: " + session.getAttribute("type") + ", Email: " + session.getAttribute("email") + ", IP: " + req.getRemoteAddr() + ".\n");
            	out.close();
                
                chain.doFilter(request, response);
            }
    	}
    }

    public void destroy() {
        //close any resources here
    }
}
