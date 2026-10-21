package ca.ulaval.glo4003.traveler.user.credential;

import ca.ulaval.glo4003.traveler.user.exception.UserLoginWrongCredentialException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserCredential {
  // PBKDF2-HMAC-SHA256 with the iteration count OWASP recommends.
  private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
  private static final int ITERATIONS = 600_000;
  private static final int SALT_BYTES = 16;
  private static final int HASH_BITS = 256;
  private static final SecureRandom RANDOM = new SecureRandom();

  private final byte[] salt;
  private final byte[] passwordHash;
  private final String email;

  public UserCredential(String email, String password) {
    this.email = email;
    this.salt = new byte[SALT_BYTES];
    RANDOM.nextBytes(this.salt);
    this.passwordHash = hash(password, this.salt);
  }

  public void validatePassword(String passwordToCompare) {
    if (passwordToCompare == null
        || !MessageDigest.isEqual(this.passwordHash, hash(passwordToCompare, this.salt))) {
      throw new UserLoginWrongCredentialException();
    }
  }

  public String getEmailAddress() {
    return this.email;
  }

  private static byte[] hash(String password, byte[] salt) {
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_BITS);
    try {
      return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("Password hashing is unavailable", e);
    } finally {
      spec.clearPassword();
    }
  }

  // Only the password hash is kept, salted at random, so two credentials are
  // equal when they belong to the same email; use validatePassword to check one.
  @Override
  public boolean equals(Object o) {
    boolean equal = false;
    if (o == this) {
      equal = true;
    } else if (o instanceof UserCredential otherCredentials) {
      equal = this.email.equals(otherCredentials.email);
    }
    return equal;
  }

  @Override
  public int hashCode() {
    return this.email.hashCode();
  }
}
