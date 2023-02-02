package com.flab.modu.domain.market.domain;


import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MarketValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Market.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // 입력값 : name, url
    // name : 존재여부, 길이 체크
    // url : 존재여부, 영어대소문자, 길이 체크

    ValidationUtils.rejectIfEmpty(errors, "name", "market.name.empty", "마켓명을 입력해주세요.");
    ValidationUtils.rejectIfEmpty(errors, "url", "market.url.empty", "마켓주소를 입력해주세요.");

    Market market = (Market) target;

    if (market.getName().length() > 200) {
      errors.rejectValue("name", "market.name.invalid", "too long name");
    }

    Pattern urlPattern = Pattern.compile("^[a-zA-Z]*$");
    if (market.getUrl().length() > 100) {
      errors.rejectValue("url", "market.url.length.invalid", "too long url");
    }
    if (!urlPattern.matcher(market.getUrl()).matches()) {
      errors.rejectValue("url", "market.url.format.invalid", "invalid url format");
    }
  }
}
