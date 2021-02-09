<template>

  <form class="form-horizontal needs-validation form-register" autocomplete="off" @submit="checkForm" action="https://vuejs.org" method="post" novalidate>

    <h1 class="h3 mb-3 font-weight-normal">Enter your registration details</h1>

    <div v-if="serverError">
      <div class="alert alert-danger">
        <p>There was an error submitting your request to the server:</p>
        <ul>
          <li v-for="error in errors">{{ error }}</li>
        </ul>
      </div>
    </div>

    <div class="form-group row">
      <div class="col-md-6">
        <label for="username" class="text-black">Username </label>
        <input type="text" class="form-control" id="username" v-model="user.username" name="username" placeholder="Enter desired username"
               :class="usernameFormObj.text != null ? (usernameFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!usernameFormObj.isValid" class="invalid-feedback">
          <span v-text="usernameFormObj.text"></span>
        </div>
        <div v-if="usernameFormObj.isValid" class="valid-feedback">
          <span v-text="usernameFormObj.text"></span>
        </div>
      </div>
    </div>

    <div class="form-group row">
      <div class="col-md-6">
        <label for="firstName" class="text-black">First Name </label>
        <input type="text" class="form-control" id="firstName" v-model="user.firstName" name="firstName"
               :class="firstNameFormObj.text != null ? (firstNameFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!firstNameFormObj.isValid" class="invalid-feedback">
          <span v-text="firstNameFormObj.text"></span>
        </div>
        <div v-if="firstNameFormObj.isValid" class="valid-feedback"> </div>
      </div>
      <div class="col-md-6">
        <label for="lastName" class="text-black">Last Name </label>
        <input type="text" class="form-control" id="lastName" v-model="user.lastName" name="lastName"
               :class="lastNameFormObj.text != null ? (lastNameFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!lastNameFormObj.isValid" class="invalid-feedback">
          <span v-text="lastNameFormObj.text"></span>
        </div>
        <div v-if="lastNameFormObj.isValid" class="valid-feedback"> </div>
      </div>
    </div>

    <div class="form-group row">
      <div class="col-md-6">
        <label for="email" class="text-black">Email </label>
        <input type="text" class="form-control" id="email" v-model="user.email" name="email" placeholder="Email Address"
               :class="emailFormObj.text != null ? (emailFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!emailFormObj.isValid" class="invalid-feedback">
          <span v-text="emailFormObj.text"></span>
        </div>
        <div v-if="emailFormObj.isValid" class="valid-feedback">
          <span v-text="emailFormObj.text"></span>
        </div>
      </div>
      <div class="col-md-6">
        <label for="phone" class="text-black">Phone </label>
        <input type="text" class="form-control" id="phone" v-model="user.phone" name="phone" placeholder="Phone Number"
               :class="phoneFormObj.text != null ? (phoneFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!phoneFormObj.isValid" class="invalid-feedback">
          <span v-text="phoneFormObj.text"></span>
        </div>
        <div v-if="phoneFormObj.isValid" class="valid-feedback">
          <span v-text="phoneFormObj.text"></span>
        </div>
      </div>
    </div>

    <div class="form-group row">
      <div class="col-md-6">
        <label for="password" class="text-black">Password </label>
        <input type="password" class="form-control" id="password" v-model="user.password" name="password" place="Enter your desired password"
               :class="passwordFormObj.text != null ? (passwordFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!passwordFormObj.isValid" class="invalid-feedback">
          <span v-text="passwordFormObj.text"></span>
        </div>
        <div v-if="passwordFormObj.isValid" class="valid-feedback"> </div>
      </div>
      <div class="col-md-6">
        <label for="confirmPassword" class="text-black">Confirm Password </label>
        <input type="password" class="form-control" id="confirmPassword" v-model="user.confirm" name="confirmPassword" placeholder="Confirm your password"
               :class="confirmFormObj.text != null ? (confirmFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!confirmFormObj.isValid" class="invalid-feedback">
          <span v-text="confirmFormObj.text"></span>
        </div>
        <div v-if="confirmFormObj.isValid" class="valid-feedback"> </div>
      </div>
    </div>

    <div class="form-group row mt-4">
      <div class="col-md-12">
        <button type="submit" class="btn btn-primary">Register</button>
      </div>
    </div>
  </form>
</template>

<script>
  module.exports = {
    name: 'register-user',
    props: {
    },
    data: function() {
      return {
        formError: false,
        serverError: false,
        errors: [], // server errors
        newUser: {},
        user: {
          username: null,
          firstName: null,
          lastName: null,
          email: null,
          phone: null,
          password: null,
          confirm: null
        },
        usernameFormObj:  { isValid: null, text: null },
        firstNameFormObj: { isValid: null, text: null },
        lastNameFormObj:  { isValid: null, text: null },
        emailFormObj:     { isValid: null, text: null },
        phoneFormObj:     { isValid: null, text: null },
        passwordFormObj:  { isValid: null, text: null },
        confirmFormObj:   { isValid: null, text: null },
      };
    },
    async created() {

    },
    methods: {
      async checkForm(e) {
        this.errors = [];

        e.preventDefault();
        e.stopPropagation();

        if (!this.user.username) {
          this.usernameFormObj.isValid = false;
          this.usernameFormObj.text = "A username is required";
          this.formError = true;
        } else if (!this.validUsername(this.user.username)) {
          this.usernameFormObj.isValid = false;
          this.usernameFormObj.text = "Username should be between 4 and 10 characters or digits";
          this.formError = true;
        } else if (await this.usernameTaken(this.user.username)) {
          this.usernameFormObj.isValid = false;
          this.usernameFormObj.text = "The username is already taken";
          this.formError = true;
        } else {
          this.usernameFormObj.isValid = true;
          this.usernameFormObj.text = "Username is available";
        }

        if (!this.user.firstName) {
          this.firstNameFormObj.isValid = false;
          this.firstNameFormObj.text = "A first name is required";
          this.formError = true;
        } else {
          this.firstNameFormObj.isValid = true;
          this.firstNameFormObj.text = "First name is valid";
        }

        if (!this.user.lastName) {
          this.lastNameFormObj.isValid = false;
          this.lastNameFormObj.text = "A lastName is required";
          this.formError = true;
        } else {
          this.lastNameFormObj.isValid = true;
          this.lastNameFormObj.text = "Last name is valid";
        }

        if (!this.user.email) {
          this.emailFormObj.isValid = false;
          this.emailFormObj.text = "An email address is required";
          this.formError = true;
        } else if (!this.validEmail(this.user.email)) {
          this.emailFormObj.isValid = false;
          this.emailFormObj.text = "A valid email address is required";
          this.formError = true;
        } else {
          this.emailFormObj.isValid = true;
          this.emailFormObj.text = "Email address is valid";
        }

        if (!this.user.phone) {
          this.phoneFormObj.isValid = false;
          this.phoneFormObj.text = "A phone numbers is required";
          this.formError = true;
        } else if (!this.validPhone(this.user.phone)) {
          this.phoneFormObj.isValid = false;
          this.phoneFormObj.text = "Phone number should be between 7 and 12 digits";
          this.formError = true;
        } else {
          this.phoneFormObj.isValid = true;
          this.phoneFormObj.text = "Phone number is valid";
        }

        if (!this.user.password) {
          this.passwordFormObj.isValid = false;
          this.passwordFormObj.text = "A password is required";
          this.formError = true;
        } else {
          this.passwordFormObj.isValid = true;
        }

        if (!this.user.confirm) {
          this.confirmFormObj.isValid = false;
          this.confirmFormObj.text = "A password confirmation is required";
          this.formError = true;
        } else if (!(String(this.user.password).valueOf() === String(this.user.confirm).valueOf())) {
          this.confirmFormObj.isValid = false;
          this.confirmFormObj.text = "Password and password confirmation fields do not match";
          this.formError = true;
        } else {
          this.confirmFormObj.isValid = true;
        }

        if (this.formError) {
          //
        } else {
          // set loading for submit button
          let axiosConfig = {
            headers: {
              'Content-Type': 'application/json;charset=UTF-8',
              "Access-Control-Allow-Origin": "*",
            }
          };
          axios.post("/api/v3/site/registerUser", JSON.stringify(this.user), axiosConfig)
              .then((response) => {
                //console.log(response);
                window.location.href = "/login?registerSuccess";
              })
              .catch(error => {
                this.serverError = true;
                if (error.response) {
                  console.log(error.response.data);
                  if (error.response.data.body) {
                    this.errors = error.response.data.body.errors
                  }
                  //console.log(error.response.status);
                  //console.log(error.response.headers);
                } else if (error.request) {
                  console.log(error.request);
                } else {
                  console.log('Error', error.message)
                }
                console.log(error.config);
              });
        }

      },
      validUsername: function(username) {
        const re = /^[a-z|0-9]{4,10}$/;
        return re.test(username);
        return true;
      },
      async usernameTaken(username) {
        try {
          const response = await axios.get("/api/v3/site/usernameAlreadyExists/" + username);
          return (response.data);
        } catch (error) {
          console.log('Error', error.message)
          return false;
        }
      },
      async emailTaken(email) {
        try {
          const response = await axios.get("/api/v3/site/emailAlreadyExists/" + email);
          return (response.data);
        } catch (error) {
          console.log('Error', error.message)
          return false;
        }
      },
      validEmail: function (email) {
        const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
      },
      validPhone: function (phone) {
        const re = /^(?!0+$)[0-9]{7,12}$/;
        return re.test(phone);
      }
    },
    computed: {

    },
    watch: {

    },
    mounted() {

    }
  };
</script>

<style scoped>
</style>
