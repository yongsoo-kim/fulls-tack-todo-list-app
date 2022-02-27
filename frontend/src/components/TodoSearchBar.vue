<template>
  <div class="form-group has-search m-3">
    <span class="fa fa-search form-control-feedback"></span>
    <input
      type="text"
      class="form-control"
      placeholder="Search"
      v-model="searchKeyInputs"
    />
  </div>
</template>

<script>
import { ref, watchEffect } from "vue";

export default {
  emits: ["searchKeyInputs"],
  setup(props, { emit }) {
    const searchKeyInputs = ref("");

    watchEffect(() => {
      if (searchKeyInputs.value != "") {
        emit("searchKeyInputs", searchKeyInputs.value);
      }
    });

    return {
      searchKeyInputs,
    };
  },
};
</script>

<style></style>
