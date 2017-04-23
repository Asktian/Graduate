package com.starry.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.starry.entity.Doctor;
import com.starry.entity.DoctorInfo;
import com.starry.service.IDoctorService;

@Controller
@RequestMapping("/")
public class DoctorController {
	@Autowired
	private IDoctorService doctorService;

	public IDoctorService getDoctorService() {
		return doctorService;
	}

	public void setDoctorService(IDoctorService doctorService) {
		this.doctorService = doctorService;
	}

	@RequestMapping(value = "Djson", produces = "application/json")
	public @ResponseBody List<DoctorInfo> getJson() {
		List<DoctorInfo> list = doctorService.selectAll();
		System.out.println("DoctorController" + list);
		return list;
	}
	@RequestMapping(value = "getAllDoctor")
	public  String selectAll(Model model) {
		List<DoctorInfo> alldoctor = doctorService.selectAll();
		model.addAttribute("alldoctor", alldoctor);
		return "allDoctor";
	}
	
	@RequestMapping(value="getById")
	public String getById(String dNumber,Model model){
		List<Doctor> doctors = doctorService.getById(dNumber);
		model.addAttribute("doctor", doctors);
		System.out.println(doctors);
		return "updateDoctor";
	}
	
	
//, method = RequestMethod.POST
	@RequestMapping(value = "Ddelete")
	public String delete( String dNumber) {
		doctorService.deleteById(dNumber);
		return "redirect:/getAllDoctor";
	} 
	
//, method = RequestMethod.POST
	@RequestMapping(value = "addDoctor",method = RequestMethod.POST)
	public String register(@RequestParam("image") MultipartFile image,
			 HttpServletRequest request,
			@RequestParam("dNumber") String dNumber,
			@RequestParam("name") String name,
			@RequestParam("dPwd") String dPwd,
			@RequestParam("cNumber") String cNumber,
			@RequestParam("dInfo") String dInfo,
			@RequestParam("dResume") String dResume,
			@RequestParam("dTel") String dTel,
			@RequestParam("dEmail") String dEmail,
			Model model) {
		// 获取项目的根路径，将上传图片的路径与我们的资源路径在一起，才能显示
		HttpSession session = request.getSession();
		String path = session.getServletContext().getRealPath("/");
		System.out.println("getRealPath('/'):" + path);
		int end = path.indexOf("t", 19);
		String prePath = path.substring(0, end);
		String realPath = "d:\\temp";
		System.out.println("DEBUG:" + realPath);
		String picName = new Date().getTime() + ".jpg";
		if (!image.isEmpty()) {
			try {
				FileUtils.copyInputStreamToFile(image.getInputStream(),
						new File(realPath, picName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		Doctor doctor = new Doctor(dNumber, name, dPwd, cNumber, dInfo,
				dResume, dTel, dEmail, "\\" + picName);
		System.out.println(doctor);
		int a = doctorService.insert(doctor);
		System.out.println("" + a);
		if (a == 1) {
			return "redirect:/getAllDoctor";
		}
		return "404";
	}


}
