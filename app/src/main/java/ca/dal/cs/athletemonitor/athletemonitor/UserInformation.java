package ca.dal.cs.athletemonitor.athletemonitor;

/**
 * This class contains personal information that will be displayed
 * on a user profile page. To instantiate an object of this class, a
 * UserInformationBuilder must be used.
 */
public class UserInformation {

	//TODO Add support for a picture
	public String firstName;
	public String lastName;
	public int age;
	/** Height in centimetres */
	public int height;
	/** Weight in kilograms */
	public int weight;
	public String athleteType;
	public String personalStatement;

	private UserInformation(UserInformationBuilder builder) {
		firstName = builder.firstName;
		lastName = builder.lastName;
		age = builder.age;
		height = builder.height;
		weight = builder.weight;
		athleteType = builder.athleteType;
		personalStatement = builder.personalStatement;
	}

	/**
	 * This class is used to instantiate a UserInformation object. It uses
	 * the builder design pattern.
	 */
	static class UserInformationBuilder {
		private String firstName;
		private String lastName;
		private int age;
		/** Height in centimetres */
		private int height;
		/** Weight in kilograms */
		private int weight;
		private String athleteType = "";
		private String personalStatement = "";

		public UserInformationBuilder(String firstName, String lastName) throws RuntimeException {
			if (firstName == null || lastName == null)
				throw new RuntimeException("Name fields must not be null!");

			this.firstName = firstName;
			this.lastName = lastName;
		}

		public UserInformationBuilder age(int age) {
			this.age = age;
			return this;
		}

		public UserInformationBuilder height(int height) {
			this.height = height;
			return this;
		}

		public UserInformationBuilder weight(int weight) {
			this.weight = weight;
			return this;
		}

		public UserInformationBuilder athleteType(String athleteType) {
			if (athleteType != null)
				this.athleteType = athleteType;
			return this;
		}

		public UserInformationBuilder personalStatement(String personalStatement) {
			if (personalStatement != null)
				this.personalStatement = personalStatement;
			return this;
		}

		public UserInformation build() {
			return new UserInformation(this);
		}
	}
}
