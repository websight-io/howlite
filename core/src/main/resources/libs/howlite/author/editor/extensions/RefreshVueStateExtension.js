export default {
  init: (editor) => {
    editor.addEventListener('component-dom-updated', () => {
      editor.componentTree.get().parentDocument.location.reload();
    });
  }
}