// Один диалог для символа и временных информационных разделов.
const infoDialog = document.getElementById('info-dialog');
const dialogTitle = document.getElementById('dialog-title');
const dialogText = document.getElementById('dialog-text');

function showInformation(title, message) {
    dialogTitle.textContent = title;
    const paragraphs = message.split(/\n\s*\n/).map(paragraph => {
        const item = document.createElement('p');
        item.className = 'dialog-paragraph';
        item.textContent = paragraph.trim();
        return item;
    });
    dialogText.replaceChildren(...paragraphs);
    infoDialog.showModal();
}

// Копируем книжную разметку только для объяснения символа.
document.getElementById('symbol-open').addEventListener('click', () => {
    dialogTitle.textContent = 'Смысл символа';
    dialogText.replaceChildren(document.getElementById('symbol-story').content.cloneNode(true));
    infoDialog.classList.add('symbol-story');
    infoDialog.showModal();
    infoDialog.scrollTop = 0;
});
document.querySelector('.dialog-dismiss').addEventListener('click', () => infoDialog.close());
infoDialog.addEventListener('close', () => infoDialog.classList.remove('symbol-story'));

document.querySelectorAll('[data-section]').forEach(button => {
    button.addEventListener('click', () => {
        const section = button.dataset.section;
        showInformation(section, section === 'Войти'
            ? 'Вход в личное пространство появится по мере развития проекта.'
            : 'Этот раздел мы будем наполнять постепенно, вдумчиво выбирая содержание.');
    });
});
