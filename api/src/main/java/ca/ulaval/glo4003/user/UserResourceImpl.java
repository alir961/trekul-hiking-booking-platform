package ca.ulaval.glo4003.user;

import ca.ulaval.glo4003.auth.authentication.AuthenticationRequired;
import ca.ulaval.glo4003.auth.authentication.LoggedIn;
import ca.ulaval.glo4003.auth.authorisation.AccessAsOwnerRequired;
import ca.ulaval.glo4003.money.Money;
import ca.ulaval.glo4003.profile.ProfileService;
import ca.ulaval.glo4003.register.RegisterService;
import ca.ulaval.glo4003.register.dto.UserDto;
import ca.ulaval.glo4003.traveler.TravelerID;
import ca.ulaval.glo4003.traveler.user.profile.Profile;
import ca.ulaval.glo4003.user.assembler.CreateUserRequestAssembler;
import ca.ulaval.glo4003.user.assembler.ProfileResponseAssembler;
import ca.ulaval.glo4003.user.request.CreateUserRequest;
import ca.ulaval.glo4003.user.response.ProfileResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/trekul/users")
public class UserResourceImpl implements UserResource {
  private final RegisterService registerService;
  private final CreateUserRequestAssembler createUserRequestAssembler;
  private final ProfileResponseAssembler profileResponseAssembler;
  private final ProfileService profileService;

  @Inject
  public UserResourceImpl(
      RegisterService registerService,
      CreateUserRequestAssembler createUserRequestAssembler,
      ProfileResponseAssembler profileResponseAssembler,
      ProfileService profileService) {
    this.registerService = registerService;
    this.createUserRequestAssembler = createUserRequestAssembler;
    this.profileResponseAssembler = profileResponseAssembler;
    this.profileService = profileService;
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUser(CreateUserRequest createUserRequest) {
    UserDto userDto = this.createUserRequestAssembler.assemble(createUserRequest);

    this.registerService.register(userDto);
    return Response.ok().build();
  }

  @GET
  @Path("/{userID}")
  @Override
  @AuthenticationRequired
  @AccessAsOwnerRequired("userID")
  public Response readProfile(@LoggedIn TravelerID travelerID, @PathParam("userID") String userID) {
    Profile profile = this.profileService.getProfile(userID);
    Money credits = this.profileService.getUserCredits(userID);
    ProfileResponse responseBody = this.profileResponseAssembler.assemble(profile, credits);

    return Response.ok(responseBody).build();
  }
}
