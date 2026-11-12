// Одна модальная форма служит для символа и будущих тематических разделов.
const infoDialog = document.getElementById('info-dialog');
const dialogTitle = document.getElementById('dialog-title');
const dialogText = document.getElementById('dialog-text');

function showInformation(title, message) {
    dialogTitle.textContent = title;
    dialogText.textContent = message;
    infoDialog.showModal();
}

document.getElementById('symbol-open').addEventListener('click', () => {
    showInformation('Смысл символа',
        'Два волка в одном круге - образ нашей внутренней сложности. Боль и надежда, страх и мужество могут жить рядом.\n\nВсем чувствам есть место. Каждый день мы выбираем, каким поступкам давать силу.');
});

document.querySelectorAll('[data-section]').forEach(button => {
    button.addEventListener('click', () => {
        const section = button.dataset.section;
        showInformation(section, section === 'Войти'
            ? 'Вход в личное пространство появится по мере развития проекта.'
            : 'Этот раздел мы будем наполнять постепенно, вдумчиво выбирая содержание.');
    });
});
