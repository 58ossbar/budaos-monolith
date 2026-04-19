package com.budaos.modules.common.qrcodeEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileDisRAR {

	/**
	 * 							创建对应的文件夹,如果state传为true，则是创建文件夹，如果为false，则删除文件夹
	 * @param path				目录路径
	 * @param fileName          文件夹的名称
	 * @param state				boolean值，如果为true，则是创建，如果为false,则是删除指定
	 * @return					返回加文件名的路径
	 */
	public String CreateORDeleteFileDirectory(String path,String fileName,boolean state){
		path = path+"/"+fileName;
		File fs = new File(path);
		
		try {
			//如果文路径对应为一个目录
			if(fs.exists()){
				
				if(fs.isDirectory()){
					//如果该目录存在，则进行删除,删除文件夹的时候需先删除文件夹里面的数据
				
					//递归删除文件
					this.DeleteFile(fs);

					//在创建
					if(state){
						fs.mkdir();
					}
				}else{
					//不是目录，为文件
					fs.delete();
					//后续业务如何处理，待定
				}
				
			}else{
				//文路径下的文件或文件夹不存在，则创建相关文件夹
				System.out.println(fs.mkdir());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return path;
	}
	
	/**
	 * 				删除文件夹递归方法
	 *@param f		文件对像
	 */
	private void DeleteFile(File f){
		
		if(f.isDirectory()){
			//读取文件夹中所有文件
			File[] fileList = f.listFiles();
			//如果该文件夹为空文件夹，则直接删除
			if(fileList.length==0){
				f.delete();
			}else{
				//递归删除
				for (File file : fileList) {
					//删除文件夹里的所有文件
					DeleteFile(file);
					//之后把自身文件夹给删除掉
					file.delete();
				}
				f.delete();
			}
		}else{
			//如果为文件，则可以直接删除
			f.delete();
		}
		
	}
	
	/**
	 * 					开始找到文件夹进行压缩,只能压缩成为ZIP的,如果用util自带的压缩包，会出现中文乱码
	 * 					这里用的是ant.jar包
	 * @param path		文件路径,比如c:/test
	 * @param fileName  文件夹      fileName
	 * 
	 */
	public void startDirectoryZip(String path,String fileName){
		
		try {
			//把压缩后的文件夹存在对应目录,创建一个写流，创建该zip
			FileOutputStream ouput = new FileOutputStream(path+"/"+fileName+".zip");
			//文件压缩存储到哪个压缩包里
			ZipOutputStream zipoutput = new ZipOutputStream(ouput);
			//得到文件夹
			File f = new File(path+"/"+fileName);
			
			if(f.exists()){	
				Zip(zipoutput,f,"");	
			}else{
				System.out.println("传入的路径不正常，找不到文件");
			}
			zipoutput.close();
			ouput.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 							压缩文件或文件夹		
	 * @param path
	 * @param fileName
	 */
	public void startFileZip(String path,String fileName){
		File f = new File(path+"/"+fileName);
		try {
			if(f.exists()){
				if(f.isDirectory()){
					startDirectoryZip(path,fileName);
				}else{
					String[] fileArr = fileName.split(".");
					//创建读取流
					FileInputStream input = new FileInputStream(f);
					//按字节读取
//					BufferedInputStream bis = new BufferedInputStream(input);
					//创建写入流
					FileOutputStream output = new FileOutputStream(path+"/"+fileArr[0]+".zip");
					//按字节流写
//					BufferedOutputStream biou = new BufferedOutputStream(output);
					
					java.util.zip.ZipOutputStream  zip = new java.util.zip.ZipOutputStream(output);//压缩包
					
					ZipEntry entry = new ZipEntry(f.getName());//压缩包名里的文件名
					
					//写入新的zip文件条目并将流定位到目标数据据的开始处
					zip.putNextEntry(entry);
					int len = 0;
					while((len = input.read())!=-1){
						zip.write(len);
						zip.flush();
					}
					
//					byte[] buf = new byte[1024];
//					while((len = bis.read(buf))!=-1){
//						zip.write(buf, 0, len);
//					}
					
					zip.close();
					input.close();
				}
				
			}else{
				System.out.println("文件不存在");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 					递归压缩文件夹
	 * @param out		压缩流
	 * @param f			文件
	 * @param fileName	文件名		
	 */
	private void Zip(ZipOutputStream zipoutput,File f,String fileName){
		try {
			if(f.isDirectory()){
				//首先压缩文件夹
				File[] fileList = f.listFiles();
				if(!"".equals(fileName)){//如果文件名称为空，不进行压缩
					ZipEntry zipEntry = new ZipEntry(fileName+"/");//压缩的包里的文件名
					zipoutput.putNextEntry(zipEntry);
				}
				
				//递归文件夹以下的目录
				fileName = fileName.length()==0?"":fileName+"/";
				//递归压缩
				for (File file : fileList) {
					Zip(zipoutput,file,fileName+file.getName());
				}	
			}else{
				//压缩文件
				ZipEntry zipEntry = new ZipEntry(fileName);//压缩的文件夹里的每一个文件名称
				zipoutput.putNextEntry(zipEntry);//放入到压缩包中
				//把文件写入读取流中
				FileInputStream input = new FileInputStream(f);//读取当前文件流
				//开始压缩
				int base = 0;
				while((base = input.read())!=-1){
					zipoutput.write(base);
					zipoutput.flush();
				}
				input.close();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 			修改文件名称
	 * @Title: renameList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param url
	 * @param @param from
	 * @param @param to 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void renameList(String url,String from,String to){
		File f = new File(url);
		if(f.exists()){
			if(f.isDirectory()){
				File[] fileList = f.listFiles();
				for (File file : fileList) {
					String name = file.getName();
					if(name.endsWith(from)){
						String newName = file.getParent() + "/"
									+ name.substring(0, name.indexOf(from)) + to;
						File newfile = new File(newName);
						if(newfile.exists())
							newfile.delete();
						file.renameTo(newfile);
					}
				}
			}
		}
	}
	
	/**
	 * 									得到路径对应文件夹下每个文件的的详细信息
	 * @param path						文件存放路径
	 * @param fileName					文件夹名称
	 * @return							返回文件夹下每个文件的详细信息，list
	 */
	public List<Object[]> getFileInfo(String path,String fileName){
		DecimalFormat df = new DecimalFormat("#.00");
		//创建存储对你
		List<Object[]> list = new ArrayList<Object[]>();
		//查询文件夹
		File f = new File(path+"/"+fileName);
		if(f.exists()){
			if(f.isDirectory()){
				//判定是否为文件夹
				//得到文件夹下所有文件
				File[] fileList = f.listFiles();
				for (File file : fileList) {
					Object[] obj = new Object[4];
//					文件名
					obj[0] = file.getName();
					if(file.length()==118){
						//如果文件为空的，则删除
						file.delete();
					}else{
						//文件大小字节为单位
						obj[1]=df.format(file.length()/1024);
						//文件类型
						String [] arrf = file.getName().split("\\.");
						if(arrf.length>1){
							obj[2] = arrf[1];
						}else{
							obj[2] = "文件";
						}
						//最后修改时间
						obj[3]=file.lastModified();
						list.add(obj);
					}
				}
				
			}else{
				//文件类型枚举
//				FileTypeMap filetypeMap = FileTypeMap.getDefaultFileTypeMap();
				//非文件夹
				Object[] obj = new Object[4];
//				//获得文件修改目期
//				System.out.println(getLongToDateString(f.lastModified()));
//				//获取文件长度
//				System.out.println(f.length());
//				//测试文件是否可读
//				System.out.println(f.canRead());
//				//测试文件是否可写
//				System.out.println(f.canWrite());
//				//测试文件是否隐藏
//				System.out.println(f.isHidden());
//				//获以文件类型
//				String [] arrf = f.getName().split("\\.");
//				System.out.println(arrf.length);
//				if(arrf.length>0){
//					System.out.println(arrf[1]);
//				}else{
//					System.out.println();
//				}
//				
//				System.out.println(filetypeMap.getContentType(f));
				//文件名
				obj[0] = f.getName();
				//文件大小字节为单位
				obj[1]=f.length();
				//文件类型
				String [] arrf = f.getName().split("\\.");
				if(arrf.length>0){
					obj[2] = arrf[1];
				}else{
					obj[2] = "文件";
				}
				//最后修改时间
				obj[3]=f.lastModified();
				list.add(0,obj);
			}
		}else{
			System.out.println("文件不存在");
		}

		return list;
	}
	
	/**
	 * 									Java文件操作 获取文件扩展名
	 * @Title: getFileNameNoEx 
	 * @Description: Java文件操作 获取不带扩展名的文件名
	 * @param @param fileName
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String getFileNameNoEx(String fileName){
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		} else {
			return "";
		}
	}
	
	/**
	 * 							用正则表达式判断字符串是否为数字
	 * @Title: isNumeric_zz 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param str
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean isNumeric_zz(String str){
		  Pattern pattern = Pattern.compile("[0-9]*"); 
		  
		  return pattern.matcher(str).matches();   
	}
	
	//public static void main(String[] args) {
//		new FileDisRAR().startDirectoryZip("d:/", "1");
//		new FileDisRAR().CreateORDeleteFileDirectory("D:/2011-06-28WEB/HSSB/WebRoot/WEB-INF/dzwjFile", "1",true);
//		List<Object[]> list = new FileDisRAR().getFileInfo("d:/", "cp936");
//		for (Object[] obj : list) {
//			System.out.println(obj[0]+" : "+obj[1]+" : "+obj[2]+" : "+obj[3]);
//		}
		
//		new FileDisRAR().startZipDecompressing("E://新建文件夹.zip", "e://122//");
//		System.out.println("成功");
		
	//	FileDisRAR rar = new FileDisRAR();
		
		//开始压缩
//		rar.startDirectoryZip("d:/", "1122");
		//String fileName = "abcde.PDF";
	//	String exd = rar.getFileNameNoEx(fileName);
		
	//	String name = fileName.substring(0,fileName.length()-exd.length()-1);
	//	System.out.println(name.indexOf("ac"));
	//}
}
