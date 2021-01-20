<template>
  <span id="user-message-count" class="badge badge-pill badge-info">{{ count }}</span>
</template>

<script>
  module.exports = {
    name: 'unread-message-count',
    data: function() {
      return {
          count: 0
      };
    },
    mounted() {
      console.log("unread-message-count::mounted")
      axios
        .get("/api/v3/messages/unread-count/" + userId, {'headers': {'Authorization': 'Bearer ' + jwtToken}})
        .then(response => (this.count = response.data))
      }
  };
</script>

<style scoped>
</style>
