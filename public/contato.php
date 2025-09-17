<?php
require_once __DIR__ . '/../bootstrap.php';

use App\Services\ContactFormHandler;

$pageTitle = 'Fale com o Trono | Contato';
$metaTitle = $pageTitle;
$metaDescription = 'Entre em contato com a equipe do Trono Remunerado para parcerias, suporte ou ideias de novas funcionalidades.';
$canonical = (string) env('APP_URL', '') !== '' ? rtrim((string) env('APP_URL', ''), '/') . '/contato' : '/contato.php';

$input = [
    'nome' => '',
    'email' => '',
    'mensagem' => '',
];
$errors = [];
$response = null;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $input['nome'] = trim((string) ($_POST['nome'] ?? ''));
    $input['email'] = trim((string) ($_POST['email'] ?? ''));
    $input['mensagem'] = trim((string) ($_POST['mensagem'] ?? ''));

    if ($input['nome'] === '') {
        $errors['nome'] = 'Como iremos anunciar sua chegada sem o seu nome real?';
    }
    if (!filter_var($input['email'], FILTER_VALIDATE_EMAIL)) {
        $errors['email'] = 'Informe um e-mail válido para que possamos responder ao seu chamado.';
    }
    if ($input['mensagem'] === '') {
        $errors['mensagem'] = 'Compartilhe sua mensagem para que a corte possa ajudar.';
    }

    if ($errors === []) {
        $handler = new ContactFormHandler();
        $response = $handler->handle($input);
        if ($response['success']) {
            $input = ['nome' => '', 'email' => '', 'mensagem' => ''];
        }
    }
}

require BASE_PATH . '/templates/partials/header.php';
?>
<section class="page-hero">
    <div class="container narrow">
        <h1>Fale com o trono</h1>
        <p>Parcerias, imprensa, ideias malucas ou sugestões de novas escalas — manda tudo pra gente.</p>
    </div>
</section>
<section class="contact">
    <div class="container contact__grid">
        <form method="post" class="contact__form" novalidate>
            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" required value="<?= sanitize_output($input['nome']); ?>">
                <?php if (isset($errors['nome'])): ?>
                    <p class="form-error"><?= sanitize_output($errors['nome']); ?></p>
                <?php endif; ?>
            </div>
            <div class="form-group">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" required value="<?= sanitize_output($input['email']); ?>">
                <?php if (isset($errors['email'])): ?>
                    <p class="form-error"><?= sanitize_output($errors['email']); ?></p>
                <?php endif; ?>
            </div>
            <div class="form-group">
                <label for="mensagem">Mensagem</label>
                <textarea id="mensagem" name="mensagem" rows="5" required><?= sanitize_output($input['mensagem']); ?></textarea>
                <?php if (isset($errors['mensagem'])): ?>
                    <p class="form-error"><?= sanitize_output($errors['mensagem']); ?></p>
                <?php endif; ?>
            </div>
            <button type="submit" class="button button--primary">Enviar mensagem</button>
            <p class="form-disclaimer">Usaremos seu e-mail apenas para responder. Nada de spam real.</p>
            <?php if ($response !== null): ?>
                <p class="form-feedback <?= $response['success'] ? 'is-success' : 'is-error'; ?>">
                    <?= sanitize_output($response['message']); ?>
                </p>
            <?php endif; ?>
        </form>
        <aside class="contact__info">
            <h2>Outros canais</h2>
            <ul>
                <li><strong>E-mail oficial:</strong> <a href="mailto:<?= sanitize_output((string) env('CONTACT_RECIPIENT_EMAIL', 'contato@teonoremunerado.com')); ?>"><?= sanitize_output((string) env('CONTACT_RECIPIENT_EMAIL', 'contato@teonoremunerado.com')); ?></a></li>
                <li><strong>Mídias sociais:</strong> @teonoremunerado</li>
                <li><strong>Horário de atendimento:</strong> Segunda a sexta, das 9h às 18h (horário de Brasília)</li>
            </ul>
        </aside>
    </div>
</section>
<?php require BASE_PATH . '/templates/partials/footer.php'; ?>
