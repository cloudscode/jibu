package org.gaixie.jibu.interceptor;  

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.aopalliance.intercept.MethodInterceptor;  
import org.aopalliance.intercept.MethodInvocation;  
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.utils.ConnectionUtils;
  
public class TransactionInterceptor implements MethodInterceptor {  

    @Override  
        public Object invoke(MethodInvocation inv) throws Throwable {  
        Object []args = inv.getArguments();
        Object obj = null;  
        Connection conn = null;
        try {  
            conn = ConnectionUtils.getConnection();
            args[0] = conn;
            obj = inv.proceed();  
            DbUtils.commitAndClose(conn);
        } catch(SQLException se) {  
            DbUtils.rollbackAndCloseQuietly(conn);
            throw new JibuException("001B-0000");  
        } catch(JibuException je) {  
            DbUtils.rollbackAndCloseQuietly(conn);
            throw new JibuException(je.getMessage());
        }
        return obj;  
    }  
}  