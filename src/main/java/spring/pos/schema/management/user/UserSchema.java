package spring.pos.schema.management.user;

import io.swagger.v3.oas.models.media.Schema;

public class UserSchema extends Schema<UserSchema> {

   @SuppressWarnings("deprecation")
   public UserSchema() {
      this.setDescription("A user in the system");
      this.addProperties("id", new Schema<Long>()
            .description("The unique identifier for the user")
            .example(1L));
      this.addProperties("username", new Schema<String>()
            .description("The username of the user")
            .example("john_doe"));
      this.addProperties("email", new Schema<String>()
            .description("The email of the user")
            .example("john.doe@example.com"));
   }
}