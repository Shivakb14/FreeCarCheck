import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.lang.*;

public class VehicleReg {

	public static void main(String[] args) {
		String data = null;
		String regex = "[A-Z]{2}\\d{2}(\\s|)[A-Z]{3}";		
		ArrayList<String> allMatches = new ArrayList<String>();
		try {
			File myObj = new File("dataFiles"+File.separator+"car_input.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				data = myReader.nextLine();
				Matcher m = Pattern.compile(regex).matcher(data);
				while(m.find()){
					allMatches.add(m.group());
				}
			}
			
			myReader.close();

			System.setProperty("webdriver.chrome.driver", "resources"+File.separator+"chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions", "--no-sandbox");
			WebDriver driver = new ChromeDriver(options);
			driver.manage().window().maximize();

			String writeOutPutFile = "";
			String outputData = null;
			File myObj1 = new File("dataFiles"+File.separator+"car_output.txt");
			Scanner myReader1 = new Scanner(myObj1);
			while (myReader1.hasNextLine()) {
				outputData = outputData + myReader1.nextLine()+"\n";
			}
			myReader1.close();

			for(String match : allMatches) {
				driver.get("https://cartaxcheck.co.uk/");
				Thread.sleep(6000);

				driver.findElement(By.xpath("//input[@aria-label='Enter the vehicle registration']")).clear();
				driver.findElement(By.xpath("//input[@aria-label='Enter the vehicle registration']")).sendKeys(match);
				driver.findElement(By.xpath("//button[normalize-space()='Free Car Check']")).click();

				Thread.sleep(3000);
				try {
					String lblRegistration = driver.findElement(By.xpath("//dt[normalize-space()='Registration']/parent::dl//dd")).getText();
					String lblMake = driver.findElement(By.xpath("//dt[normalize-space()='Make']/parent::dl//dd")).getText();
					String lblModel = driver.findElement(By.xpath("//dt[normalize-space()='Model']/parent::dl//dd")).getText();
					String lblColor = driver.findElement(By.xpath("//dt[normalize-space()='Colour']/parent::dl//dd")).getText();
					String lblYear = driver.findElement(By.xpath("//dt[normalize-space()='Year']/parent::dl//dd")).getText();				

					if(outputData.contains(lblRegistration) && !(lblRegistration.equals(""))) {
						writeOutPutFile = writeOutPutFile+"------Data for Registration Number: "+lblRegistration+"------\n";
						writeOutPutFile = writeOutPutFile+"Reg No: "+lblRegistration+" is displayed - PASS\n";
						writeOutPutFile = writeOutPutFile+"Make: "+lblMake+"is displayed - PASS\n";
						writeOutPutFile = writeOutPutFile+"Model: "+lblModel+"is displayed - PASS\n";
						writeOutPutFile = writeOutPutFile+"Color: "+lblColor+" is displayed - PASS\n";
						writeOutPutFile =writeOutPutFile+"Year: "+lblYear+" is displayed - PASS\n\n";
					}else
						writeOutPutFile = writeOutPutFile+ "Reg No: "+match+" is NOT displayed - FAIL\n\n";
				}catch(Exception e) {
					writeOutPutFile = writeOutPutFile+ "Reg No: "+match+" is NOT displayed - FAIL\n\n";
				}
			}
			FileWriter myWriter = new FileWriter("Result.txt");
			myWriter.write(writeOutPutFile);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
			driver.close();
			driver.quit();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}


