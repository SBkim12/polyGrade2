package poly.controller;

import static poly.util.CmmUtil.nvl;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PracticeController {

	private Logger log = Logger.getLogger(this.getClass());
	
	@RequestMapping(value="table")
	public String table() {
		log.info("시작합니다");
		
		return "/table";
	}
	
	//get방식으로 받아온 값을 주소창에 보여주면서 사이트를 엶
	@RequestMapping(value="get")
	public String get(HttpServletRequest request, ModelMap model) {
		String name = nvl(request.getParameter("name"));
		log.info("get 시작!!!");
		log.info("name : " + name);
		
		// view(JSP)에 name이라는 문자열을 전달
		model.addAttribute("name", name);
		
		return "/get";
	}
	
	//포스트방식으로 사이트를 엶
	@RequestMapping(value="post")
	public String post() {
		
		log.info("post 시작합니다");
		
		return "/form";
	}
	
	//포스트방식으로 정보를 가져올때 사용
	@RequestMapping(value="doPost", method = RequestMethod.POST)
	public String doPost(HttpServletRequest request, ModelMap model) {
		String name = nvl(request.getParameter("name"));
		log.info("post 시작!!!");
		log.info("name : " + name);
		
		// view(JSP)에 name이라는 문자열을 전달
		model.addAttribute("name", name);
		
		return "/get";
	}

}
