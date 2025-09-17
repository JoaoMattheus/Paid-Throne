<?php
require_once __DIR__ . '/../bootstrap.php';

$pageTitle = 'Política de Privacidade | Trono Remunerado';
$metaTitle = $pageTitle;
$metaDescription = 'Saiba como o site Trono Remunerado trata seus dados e anúncios. Transparência total com a realeza.';
$canonical = (string) env('APP_URL', '') !== '' ? rtrim((string) env('APP_URL', ''), '/') . '/privacidade' : '/privacidade.php';

require BASE_PATH . '/templates/partials/header.php';
?>
<section class="page-hero">
    <div class="container narrow">
        <h1>Política de Privacidade</h1>
        <p>Transparência real: explicamos como lidamos com informações, anúncios e métricas.</p>
    </div>
</section>
<section class="privacy">
    <div class="container">
        <article>
            <h2>1. Dados coletados</h2>
            <p>
                A calculadora do Trono Remunerado é 100% client-side. Isso significa que os dados preenchidos no formulário não são
                enviados para nossos servidores e desaparecem assim que você sai da página ou atualiza o navegador.
            </p>
            <p>
                Ao entrar em contato conosco, pedimos apenas nome, e-mail e mensagem. Essas informações são utilizadas exclusivamente
                para responder ao seu chamado real.
            </p>
        </article>
        <article>
            <h2>2. Cookies e rastreamento</h2>
            <p>
                Utilizamos Google Analytics (caso ativado via variáveis de ambiente) para entender como o site é usado. Os dados são
                agregados e anonimizados, seguindo as políticas da ferramenta. Você pode desativar o Analytics removendo a variável <code>ANALYTICS_ID</code>.
            </p>
        </article>
        <article>
            <h2>3. Publicidade</h2>
            <p>
                Para monetizar o reino, podemos exibir anúncios do Google Adsense. O funcionamento desses anúncios segue as diretrizes do Google,
                que pode utilizar cookies e identificadores para personalizar a experiência. Recomendamos revisar a <a href="https://policies.google.com/technologies/ads?hl=pt-BR" target="_blank" rel="noopener">política de privacidade do Google</a>.
            </p>
        </article>
        <article>
            <h2>4. Armazenamento e segurança</h2>
            <p>
                Mensagens enviadas pelo formulário de contato são encaminhadas para o e-mail oficial informado nas variáveis de ambiente.
                Como contingência, mantemos um registro local protegido (arquivo fora do repositório) para garantir que nenhum pedido se perca
                enquanto ajustamos a integração de e-mail.
            </p>
        </article>
        <article>
            <h2>5. Direitos da realeza</h2>
            <p>
                Você pode solicitar a remoção de qualquer mensagem armazenada enviando um e-mail para <a href="mailto:<?= sanitize_output((string) env('CONTACT_RECIPIENT_EMAIL', 'contato@teonoremunerado.com')); ?>"><?= sanitize_output((string) env('CONTACT_RECIPIENT_EMAIL', 'contato@teonoremunerado.com')); ?></a>.
                Não trabalhamos com cadastros ou newsletters, então não há dados adicionais para remover.
            </p>
        </article>
        <article>
            <h2>6. Alterações na política</h2>
            <p>
                Sempre que atualizarmos esta página, informaremos a data da última alteração logo abaixo. Recomendamos revisitar periodicamente.
            </p>
            <p class="privacy__updated">Última atualização: <?= date('d/m/Y'); ?></p>
        </article>
    </div>
</section>
<?php require BASE_PATH . '/templates/partials/footer.php'; ?>
