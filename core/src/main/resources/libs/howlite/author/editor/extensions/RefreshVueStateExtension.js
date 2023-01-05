export default {
  init: (editor) => {
    editor.addEventListener('component tree refresh', () => {
      editor.componentTree.get().parentDocument.location.reload();
    });
  }
}