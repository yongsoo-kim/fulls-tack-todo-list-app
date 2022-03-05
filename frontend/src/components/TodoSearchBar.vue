<template>
  <div class="form-group has-search m-3">
    <span class="fa fa-search form-control-feedback"></span>
    <input
      type="text"
      class="form-control"
      placeholder="Search"
      @keyup="searchTodo"
      @keyup.enter="$emit('instant-searchTodo')"
    />
  </div>
</template>

<script>
export default {
  emits: ["search-key-input", "instant-searchTodo"],
  setup(props, { emit }) {
    const searchTodo = (e) => {
      const searchKeyInputs = e.target.value;
      emit("search-key-input", searchKeyInputs);
    };

    return {
      searchTodo,
    };
  },
};
</script>

<style></style>
