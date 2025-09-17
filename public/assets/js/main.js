(() => {
    const toggle = document.querySelector('.header__toggle');
    const nav = document.querySelector('.header__nav');
    const salarySelect = document.querySelector('#salaryType');
    const salaryHint = document.querySelector('[data-salary-hint]');
    const scheduleSelect = document.querySelector('#workSchedule');
    const scheduleHint = document.querySelector('[data-schedule-hint]');

    if (toggle && nav) {
        toggle.addEventListener('click', () => {
            const isOpen = nav.classList.toggle('is-open');
            toggle.setAttribute('aria-expanded', String(isOpen));
        });
    }

    const updateHint = (select, target) => {
        if (!select || !target) {
            return;
        }
        let hints = {};
        if (select.dataset.hints) {
            try {
                hints = JSON.parse(select.dataset.hints);
            } catch (error) {
                console.warn('Não foi possível interpretar as dicas do formulário.', error);
            }
        }
        const value = select.value;
        target.textContent = hints[value] ?? '';
    };

    if (salarySelect && salaryHint) {
        updateHint(salarySelect, salaryHint);
        salarySelect.addEventListener('change', () => updateHint(salarySelect, salaryHint));
    }

    if (scheduleSelect && scheduleHint) {
        updateHint(scheduleSelect, scheduleHint);
        scheduleSelect.addEventListener('change', () => updateHint(scheduleSelect, scheduleHint));
    }
})();
