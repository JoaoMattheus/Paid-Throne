(function () {
  const tabButtons = document.querySelectorAll('[data-tab-target]');
  tabButtons.forEach((button) => {
    button.addEventListener('click', () => {
      const target = button.dataset.tabTarget;
      document.querySelectorAll('[data-tab-panel]').forEach((panel) => {
        panel.hidden = panel.dataset.tabPanel !== target;
      });
      tabButtons.forEach((btn) => btn.classList.toggle('is-active', btn === button));
    });
  });

  const shareButton = document.querySelector('[data-share]');
  if (shareButton && navigator.share) {
    shareButton.addEventListener('click', async () => {
      try {
        await navigator.share({
          title: document.title,
          text: shareButton.dataset.shareText || 'Olha meu reinado no Trono Remunerado!',
          url: window.location.href,
        });
      } catch (error) {
        console.warn('Compartilhamento cancelado', error);
      }
    });
  }

  const form = document.querySelector('[data-throne-form]');
  if (form) {
    let lastSubmit = 0;
    form.addEventListener('submit', (event) => {
      const now = Date.now();
      if (now - lastSubmit < 3000) {
        event.preventDefault();
        const alert = document.createElement('div');
        alert.className = 'alert';
        alert.textContent = 'Segura a coroa! Espere um instante antes de enviar de novo.';
        form.prepend(alert);
        setTimeout(() => alert.remove(), 3000);
        return;
      }
      lastSubmit = now;
    });
  }
})();
