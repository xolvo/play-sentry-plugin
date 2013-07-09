package play.modules.sentry.bindings;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.kencochrane.raven.marshaller.json.InterfaceBinding;

import org.apache.commons.lang.StringUtils;

import play.modules.sentry.interfaces.PlayHttpRequestInterface;
import play.mvc.Http.Cookie;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import play.templates.JavaExtensions;

import com.fasterxml.jackson.core.JsonGenerator;

public class PlayHttpRequestInterfaceBinding implements InterfaceBinding<PlayHttpRequestInterface> {
	private static final String URL = "url";
    private static final String METHOD = "method";
    private static final String DATA = "data";
    private static final String QUERY_STRING = "query_string";
    private static final String COOKIES = "cookies";
    private static final String HEADERS = "headers";
    private static final String ENVIRONMENT = "env";
    private static final String ENV_REMOTE_ADDR = "REMOTE_ADDR";
    private static final String ENV_SERVER_NAME = "SERVER_NAME";
    private static final String ENV_SERVER_PORT = "SERVER_PORT";
    private static final String ENV_LOCAL_ADDR = "LOCAL_ADDR";
    private static final String ENV_LOCAL_NAME = "LOCAL_NAME";
    private static final String ENV_SERVER_PROTOCOL = "SERVER_PROTOCOL";
    private static final String ENV_REQUEST_SECURE = "REQUEST_SECURE";
    private static final String ENV_REMOTE_USER = "REMOTE_USER";
    
    private static String buildUrl(Request request) {
		return request.getBase() + request.path;
    }
    
	@Override
	public void writeInterface(JsonGenerator generator, PlayHttpRequestInterface playHttpInterface) throws IOException {
		Request request = playHttpInterface.getRequest();
		
		 generator.writeStartObject();
		 generator.writeStringField(URL, buildUrl(request));
		 generator.writeStringField(METHOD, request.method);
		 
		 generator.writeFieldName(DATA);
		 writeData(generator, request.params.data);
		 
		 generator.writeStringField(QUERY_STRING, request.querystring);
		 
		 generator.writeFieldName(COOKIES);
		 writeCookies(generator, request.cookies);
		 
		 generator.writeFieldName(HEADERS);
		 writeHeaders(generator, request.headers);
		 
		 generator.writeFieldName(ENVIRONMENT);
		 writeEnvironment(generator, request);
		 
		 generator.writeEndObject();
	}

	private void writeEnvironment(JsonGenerator generator, Request request) throws IOException {
		InetAddress localhost = InetAddress.getLocalHost();
		
		generator.writeStartObject();
        generator.writeStringField(ENV_REMOTE_ADDR, request.remoteAddress);
        generator.writeStringField(ENV_SERVER_NAME, request.domain);
        generator.writeNumberField(ENV_SERVER_PORT, request.port);
        generator.writeStringField(ENV_LOCAL_ADDR, localhost.getHostAddress());
        generator.writeStringField(ENV_LOCAL_NAME, localhost.getHostName());
        generator.writeStringField(ENV_SERVER_PROTOCOL, "HTTP/1.1");
        generator.writeBooleanField(ENV_REQUEST_SECURE, request.secure);
        generator.writeStringField(ENV_REMOTE_USER, request.user);
        generator.writeEndObject();
	}

	private void writeHeaders(JsonGenerator generator, Map<String, Header> headers) throws IOException {
		generator.writeStartObject();
        for(String header_key : headers.keySet()) {
            generator.writeArrayFieldStart(formatHeaderKey(header_key));
            for (String headerValue : headers.get(header_key).values) {
                generator.writeString(headerValue);
            }
            generator.writeEndArray();
        }
        generator.writeEndObject();
	}
	
	private String formatHeaderKey(String header) {
		String[] parts = header.split("-");
		List<String> args = new ArrayList<String>();
		
		for(String part : parts) {
			args.add(JavaExtensions.capFirst(part));
		}
		
		return StringUtils.join(args, "-");
	}

	private void writeCookies(JsonGenerator generator, Map<String, Cookie> cookies) throws IOException {
		if(cookies == null) {
            generator.writeNull();
            return;
        }

        generator.writeStartObject();
        for(String cookie_key : cookies.keySet()) {
        	Cookie cookie = cookies.get(cookie_key);
            generator.writeStringField(cookie.name, cookie.value);
        }
        
        generator.writeEndObject();
	}

	private void writeData(JsonGenerator generator, Map<String, String[]> parameterMap) throws IOException {
		if(parameterMap == null) {
            generator.writeNull();
            return;
        }

        generator.writeStartObject();
        for(Map.Entry<String, String[]> parameter : parameterMap.entrySet()) {
            generator.writeArrayFieldStart(parameter.getKey());
            for(String parameterValue : parameter.getValue()) {
                generator.writeString(parameterValue);
            }
            generator.writeEndArray();
        }
        generator.writeEndObject();
	}
}
