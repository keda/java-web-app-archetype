package ${package}.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

public class DateTimeFormatter implements Formatter<Date> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private String pattern;

	public DateTimeFormatter(String pattern){
		this.pattern = pattern;
	}
	
	@Override
	public String print(Date object, Locale locale) {
		logger.info("parse date....");
		if(object == null) return null;
		
		return getDateFormat(locale).format(object);
		
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		logger.info("parse date....");
		if(text.length() == 0){
			return null;
		}
		
		return getDateFormat(locale).parse(text);
		
	}
	
	private DateFormat getDateFormat(Locale locale) {
		DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
		
		dateFormat.setLenient(false);
		
		return dateFormat;
	}

}
