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
      <div class="col-md-12">
        <label for="username" class="text-black">Username <span class="text-danger">*</span></label>
        <div class="input-group">
          <input type="text" class="form-control" id="username" v-model="user.username" name="username" aria-describedby="usernameHelpBlock"
                 :class="usernameFormObj.text != null ? (usernameFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
          <div class="input-group-prepend">
            <button class="btn btn-outline-secondary" type="button" @click="checkUsername">Check Username</button>
          </div>
        </div>
        <small id="usernameHelpBlock" class="form-text text-muted">
          Bust be 6-20 characters long, containing letters and numbers, and not spaces, special characters, or emojis.
        </small>
      </div>
      <div class="col-md-12">
        <div v-if="!usernameFormObj.isValid" class="invalid-feedback" style="display: block">
          <span v-text="usernameFormObj.text"></span>
        </div>
        <div v-if="usernameFormObj.isValid" class="valid-feedback" style="display: block">
          <span v-text="usernameFormObj.text"></span>
        </div>
      </div>
    </div>

    <div class="form-group row">
      <div class="col-md-6">
        <label for="firstName" class="text-black">First Name <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="firstName" v-model="user.firstName" name="firstName"
               :class="firstNameFormObj.text != null ? (firstNameFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <div v-if="!firstNameFormObj.isValid" class="invalid-feedback">
          <span v-text="firstNameFormObj.text"></span>
        </div>
        <div v-if="firstNameFormObj.isValid" class="valid-feedback"> </div>
      </div>
      <div class="col-md-6">
        <label for="lastName" class="text-black">Last Name <span class="text-danger">*</span></label>
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
        <label for="email" class="text-black">Email <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="email" v-model="user.email" name="email" aria-describedby="emailHelpBlock"
               :class="emailFormObj.text != null ? (emailFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <small id="emailHelpBlock" class="form-text text-muted">
          A valid email address.
        </small>
        <div v-if="!emailFormObj.isValid" class="invalid-feedback" style="display: block">
          <span v-text="emailFormObj.text"></span>
        </div>
        <div v-if="emailFormObj.isValid" class="valid-feedback" style="display: block">
          <span v-text="emailFormObj.text"></span>
        </div>
      </div>
      <div class="col-md-6">
        <label for="phone" class="text-black">Phone <span class="text-danger">*</span></label>
        <input type="text" class="form-control" id="phone" v-model="user.phone" name="phone" aria-describedby="phoneHelpBlock"
               :class="phoneFormObj.text != null ? (phoneFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <small id="phoneHelpBlock" class="form-text text-muted">
          Must be 7-12 digits long.
        </small>
        <div v-if="!phoneFormObj.isValid" class="invalid-feedback" style="display: block">
          <span v-text="phoneFormObj.text"></span>
        </div>
        <div v-if="phoneFormObj.isValid" class="valid-feedback" style="display: block">
          <span v-text="phoneFormObj.text"></span>
        </div>
      </div>
    </div>

    <div class="form-group row">
      <div class="col-md-6">
        <label for="password" class="text-black">Password <span class="text-danger">*</span></label>
        <input type="password" class="form-control" id="password" v-model="user.password" name="password" aria-describedby="passwordHelpBlock"
               :class="passwordFormObj.text != null ? (passwordFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <small id="passwordHelpBlock" class="form-text text-muted">
          Must be 8-20 characters long, containing letters, numbers and special characters.
        </small>
        <div v-if="!passwordFormObj.isValid" class="invalid-feedback" style="display: block">
          <span v-text="passwordFormObj.text"></span>
        </div>
        <div v-if="passwordFormObj.isValid" class="valid-feedback" style="display: block"> </div>
      </div>
      <div class="col-md-6">
        <label for="confirmPassword" class="text-black">Confirm Password <span class="text-danger">*</span></label>
        <input type="password" class="form-control" id="confirmPassword" v-model="user.confirm" name="confirmPassword" aria-describedby="confirmPasswordHelpBlock"
               :class="confirmFormObj.text != null ? (confirmFormObj.isValid ? 'is-valid' : 'is-invalid') : ''">
        <small id="confirmPasswordHelpBlock" class="form-text text-muted">
          Confirm your password.
        </small>
        <div v-if="!confirmFormObj.isValid" class="invalid-feedback" style="display: block">
          <span v-text="confirmFormObj.text"></span>
        </div>
        <div v-if="confirmFormObj.isValid" class="valid-feedback" style="display: block"> </div>
      </div>
    </div>

    <div class="form-group row mt-4">
      <div class="col-md-12">
        <button type="submit" class="btn btn-primary">Register</button>
      </div>
    </div>
  </form>
</template>

<script src="/js/components/RegisterUser.js"></script>

<style scoped>
</style>
