package TestStore.TestStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest {

	WebDriver driver = new ChromeDriver();
	Connection con;
	Statement stmt;

	ResultSet rs;

	int customerNumberIntoDatabase;
	String customerFirstNameIntoDatabase;
	String customerLastNameIntoDatabase;

	String Email;
	String phoneIntoDatabase;
	String AddresInToDatabase;
	String cityInToDatabase;
	String postalCodeInToDatabase;
	String userName;
	String password;
	String confirmPassword;

	String Website = "http://www.automationteststore.com";
	Random rand = new Random();

	@BeforeTest
	public void mySetUp() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "1234");

		driver.get(Website);

		driver.manage().window().maximize();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 400)");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		/////// Read data from database

		String query = "select * from customers where customerNumber=114";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			// Personal Details
			customerNumberIntoDatabase = rs.getInt("customerNumber");

			customerFirstNameIntoDatabase = rs.getString("contactFirstName");

			System.out.println(customerFirstNameIntoDatabase + "@@@@@@@@@@@");
			customerLastNameIntoDatabase = rs.getString("contactLastName");

			Email = customerFirstNameIntoDatabase + customerLastNameIntoDatabase + "@gmail.com";
			phoneIntoDatabase = rs.getString("phone");

			System.out.println(phoneIntoDatabase + "@@@@@@");

			//// Address

			AddresInToDatabase = rs.getString("addressLine1");
			cityInToDatabase = rs.getString("city");

			postalCodeInToDatabase = rs.getString("postalCode");

			/// Login Details

			userName = customerFirstNameIntoDatabase + customerLastNameIntoDatabase;

			password = customerFirstNameIntoDatabase + "R@0pass";

			confirmPassword = password;

		}

	}

	@Test(priority = 1)

	public void loginOrRegister() {

		WebElement loginButton = driver.findElement(By.linkText("Login or register"));
		loginButton.click();

	}

	@Test(priority = 2, enabled = false)

	public void creatUser() throws SQLException {

		WebElement ContinueButton = driver.findElement(By.xpath("//button[@title='Continue']"));
		ContinueButton.click();

		// full Personal Details
		WebElement firstName = driver.findElement(By.id("AccountFrm_firstname"));
		firstName.sendKeys(customerFirstNameIntoDatabase);
		WebElement lastName = driver.findElement(By.id("AccountFrm_lastname"));
		lastName.sendKeys(customerLastNameIntoDatabase);

		WebElement email = driver.findElement(By.id("AccountFrm_email"));
		email.sendKeys(Email);

		WebElement phone = driver.findElement(By.id("AccountFrm_telephone"));
		phone.sendKeys(phoneIntoDatabase);

		//// full Address

		WebElement Address = driver.findElement(By.id("AccountFrm_address_1"));
		Address.sendKeys(AddresInToDatabase);

		WebElement city = driver.findElement(By.id("AccountFrm_city"));
		city.sendKeys(cityInToDatabase);

		WebElement ZipCode = driver.findElement(By.id("AccountFrm_postcode"));
		ZipCode.sendKeys(postalCodeInToDatabase);
		WebElement country = driver.findElement(By.id("AccountFrm_country_id"));

		Select myselectcountry = new Select(country);
		myselectcountry.selectByVisibleText("Jordan");
		WebElement mySelectState = driver.findElement(By.id("AccountFrm_zone_id"));

		Select myselect = new Select(mySelectState);

		myselect.selectByValue("1704");
		///// full Login Details

		WebElement userNameinput = driver.findElement(By.id("AccountFrm_loginname"));
		userNameinput.sendKeys(userName);
		WebElement pass = driver.findElement(By.id("AccountFrm_password"));
		pass.sendKeys(password);

		WebElement confirmpass = driver.findElement(By.id("AccountFrm_confirm"));
		confirmpass.sendKeys(confirmPassword);

		/////// Agree to terms and submit the form

		WebElement greeTerms = driver.findElement(By.id("AccountFrm_agree"));
		greeTerms.click();

		WebElement continueButton = driver.findElement(By.cssSelector(".btn.btn-orange.pull-right.lock-on-click"));
		continueButton.click();

	}

	@Test(priority = 3)
	public void ReturningCustomer() {

		if (userName != null && password != null) {
			WebElement loginName = driver.findElement(By.id("loginFrm_loginname"));
			loginName.sendKeys(userName);

			WebElement passwordField = driver.findElement(By.id("loginFrm_password"));
			passwordField.sendKeys(password);

			WebElement login = driver.findElement(By.xpath("//button[@title='Login']"));
			login.click();
		} else {
			System.out.println("❌ بيانات الدخول غير موجودة! تأكد من إنشاء المستخدم أولاً.");
		}
	}

	// Add one Random item from the "Men" category
	@Test(priority = 4)
	public void AddRandomItems() throws InterruptedException {

		WebElement MenCategory = driver.findElement(By.cssSelector("a[href*='category&path=58']"));

		MenCategory.click();

		List<WebElement> item = driver.findElements(By.cssSelector(".col-md-2.col-sm-2.col-xs-6.align_center"));

		int randitem = rand.nextInt(item.size());

		Thread.sleep(2000);
		item.get(randitem).click();

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 500)");

	}

	@Test(priority = 5)

	public void RandomInternalItem() throws InterruptedException {

		List<WebElement> internalItem = driver.findElements(By.cssSelector(".col-md-3.col-sm-6.col-xs-12"));

		int randominternalItem = rand.nextInt(internalItem.size());

		internalItem.get(randominternalItem).click();
		Thread.sleep(2000);

		WebElement AddToCart = driver.findElement(By.xpath("//a[contains(@class,'cart')]"));
		AddToCart.click();

		WebElement QuantityInput = driver.findElement(By.cssSelector(".form-control.short"));

		int RandomQuantity = rand.nextInt(2, 10);

		QuantityInput.clear();
		QuantityInput.sendKeys(String.valueOf(RandomQuantity));
	}

	@Test(priority = 6, enabled = false)
	public void randomRemoveItem() {

		List<WebElement> removeitem = driver.findElements(By.cssSelector(".btn.btn-sm.btn-default"));

		int randomRemoveItem = rand.nextInt(removeitem.size());

		removeitem.get(randomRemoveItem).click();

	}

	@Test(priority = 7)
	public void Checkout() {

		WebElement checkout = driver.findElement(By.id("cart_checkout1"));

		checkout.click();

		driver.findElement(By.id("checkout_btn")).click();
	    String order=driver.findElement(By.xpath("//span[@class='maintext']")).getText();
		
	    System.out.println(order);

	}

	@AfterTest
	public void afterTest() {

	}
}
