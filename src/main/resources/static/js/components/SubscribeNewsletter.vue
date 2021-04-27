<template>

  <div class="confirmation-modal">
    <b-input-group>
      <b-form-input v-model="userEmail" placeholder="Your email address"></b-form-input>
      <div class="input-group-append">
        <b-button ref="newsletter-button" @click="registerUser">
          <i class="fa fa-envelope"></i>
        </b-button>
      </div>
    </b-input-group>

    <b-modal class="confirmation-modal" id="confirmation-modal" ref="confirmation-modal" title="Newsletter Registration" ok-only>
      <div class="m-4 text-center"> {{ feedbackMessage }} </div>
    </b-modal>
  </div>

</template>

<script src="/js/components/SubscribeNewsletter.js"></script>

<style scoped>
.confirmation-modal {
  line-height: 1.7 !important;
  color: #8c92a0 !important;
  font-weight: 300 !important;
  font-size: 16px !important;
}
</style>
