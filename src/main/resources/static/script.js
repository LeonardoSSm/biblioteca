// Configuração da API
const API_BASE_URL = 'http://localhost:8080/api';

// Elementos da interface
let currentTab = 'autores';
let editingItem = null;

// Inicialização quando o DOM estiver carregado
document.addEventListener('DOMContentLoaded', function() {
    initializeTabs();
    initializeModals();
    loadInitialData();
    
    // Configurar listeners de busca
    document.getElementById('busca-autor').addEventListener('input', debounce(() => loadAutores(), 300));
    document.getElementById('busca-livro').addEventListener('input', debounce(() => loadLivros(), 300));
    document.getElementById('busca-aluno').addEventListener('input', debounce(() => loadAlunos(), 300));
    document.getElementById('filtro-status').addEventListener('change', () => loadEmprestimos());
});

// Inicializar sistema de abas
function initializeTabs() {
    const tabLinks = document.querySelectorAll('nav a');
    tabLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Atualizar aba ativa
            tabLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');
            
            // Mostrar conteúdo correspondente
            const tabName = this.getAttribute('data-tab');
            document.querySelectorAll('.tab-content').forEach(tab => {
                tab.classList.remove('active');
            });
            document.getElementById(tabName).classList.add('active');
            
            currentTab = tabName;
            
            // Carregar dados da aba selecionada
            switch(tabName) {
                case 'autores':
                    loadAutores();
                    break;
                case 'livros':
                    loadLivros();
                    break;
                case 'alunos':
                    loadAlunos();
                    break;
                case 'emprestimos':
                    loadEmprestimos();
                    break;
            }
        });
    });
}

// Inicializar modais
function initializeModals() {
    const modal = document.getElementById('modal');
    const closeBtn = document.querySelector('.close');
    const cancelBtn = document.getElementById('btn-cancelar');
    
    // Fechar modal ao clicar no X ou no botão cancelar
    closeBtn.addEventListener('click', () => closeModal());
    cancelBtn.addEventListener('click', () => closeModal());
    
    // Fechar modal ao clicar fora dele
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });
    
    // Botões para abrir modais de criação
    document.getElementById('btn-novo-autor').addEventListener('click', () => openAutorModal());
    document.getElementById('btn-novo-livro').addEventListener('click', () => openLivroModal());
    document.getElementById('btn-novo-aluno').addEventListener('click', () => openAlunoModal());
    document.getElementById('btn-novo-emprestimo').addEventListener('click', () => openEmprestimoModal());
    
    // Botão salvar do modal
    document.getElementById('btn-salvar').addEventListener('click', () => saveModalData());
}

// Carregar dados iniciais
function loadInitialData() {
    loadAutores();
}

// Função para carregar autores
async function loadAutores() {
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/autores`);
        console.log(response);
        if (!response.ok) throw new Error('Erro ao carregar autores');
        
        const autores = await response.json();
        console.log(autores);
        displayAutores(autores);
    } catch (error) {
        showError('Erro ao carregar autores: ' + error.message);
    } finally {
        hideLoading();
    }
}

// Exibir autores na lista
function displayAutores(autores) {
    const lista = document.getElementById('lista-autores');
    const searchTerm = document.getElementById('busca-autor').value.toLowerCase();
    
    // Filtrar autores se houver termo de busca
    const autoresFiltrados = searchTerm 
        ? autores.filter(autor => 
            autor.nome.toLowerCase().includes(searchTerm) || 
            (autor.nacionalidade && autor.nacionalidade.toLowerCase().includes(searchTerm)))
        : autores;
    
    if (autoresFiltrados.length === 0) {
        lista.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-users"></i>
                <p>Nenhum autor encontrado</p>
            </div>
        `;
        return;
    }
    
    lista.innerHTML = autoresFiltrados.map(autor => `
        <div class="data-item">
            <div class="data-info">
                <h4>${autor.nome}</h4>
                <p>Nacionalidade: ${autor.nacionalidade || 'Não informada'}</p>
                <p>ID: ${autor.id}</p>
            </div>
            <div class="data-actions">
                <button class="btn btn-primary" onclick="openAutorModal(${autor.id})">
                    <i class="fas fa-edit"></i> Editar
                </button>
                <button class="btn btn-danger" onclick="deleteAutor(${autor.id})">
                    <i class="fas fa-trash"></i> Excluir
                </button>
            </div>
        </div>
    `).join('');
}

// Função para carregar livros
async function loadLivros() {
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/livros`);
        if (!response.ok) throw new Error('Erro ao carregar livros');
        
        const livros = await response.json();
        displayLivros(livros);
    } catch (error) {
        showError('Erro ao carregar livros: ' + error.message);
    } finally {
        hideLoading();
    }
}

// Exibir livros na lista
function displayLivros(livros) {
    const lista = document.getElementById('lista-livros');
    const searchTerm = document.getElementById('busca-livro').value.toLowerCase();
    
    // Filtrar livros se houver termo de busca
    const livrosFiltrados = searchTerm 
        ? livros.filter(livro => 
            livro.titulo.toLowerCase().includes(searchTerm) || 
            (livro.isbn && livro.isbn.toLowerCase().includes(searchTerm)))
        : livros;
    
    if (livrosFiltrados.length === 0) {
        lista.innerHTML = `
            <div class="empty-state">
                <i class="fas fa-book"></i>
                <p>Nenhum livro encontrado</p>
            </div>
        `;
        return;
    }
    
    lista.innerHTML = livrosFiltrados.map(livro => `
        <div class="data-item">
            <div class="data-info">
                <h4>${livro.titulo}</h4>
                <p>ISBN: ${livro.isbn || 'Não informado'}</p>
                <p>Ano: ${livro.anoPublicacao || 'Não informado'}</p>
                <p>Autor: ${livro.autor ? livro.autor.nome : 'Não atribuído'}</p>
            </div>
            <div class="data-actions">
                <button class="btn btn-primary" onclick="openLivroModal(${livro.id})">
                    <i class="fas fa-edit"></i> Editar
                </button>
                <button class="btn btn-danger" onclick="deleteLivro(${livro.id})">
                    <i class="fas fa-trash"></i> Excluir
                </button>
            </div>
        </div>
    `).join('');
}

// Funções para Alunos e Empréstimos (estrutura similar)
async function loadAlunos() {
    // Implementação similar às funções de autores e livros
    document.getElementById('lista-alunos').innerHTML = `
        <div class="empty-state">
            <i class="fas fa-user-graduate"></i>
            <p>Funcionalidade de alunos em desenvolvimento</p>
        </div>
    `;
}

async function loadEmprestimos() {
    // Implementação similar às funções de autores e livros
    document.getElementById('lista-emprestimos').innerHTML = `
        <div class="empty-state">
            <i class="fas fa-exchange-alt"></i>
            <p>Funcionalidade de empréstimos em desenvolvimento</p>
        </div>
    `;
}

// Modal para autores
async function openAutorModal(autorId = null) {
    editingItem = autorId;
    const modal = document.getElementById('modal');
    const form = document.getElementById('modal-form');
    
    // Configurar título do modal
    document.getElementById('modal-titulo').textContent = 
        autorId ? 'Editar Autor' : 'Novo Autor';
    
    // Carregar dados do autor se for edição
    let autorData = {};
    if (autorId) {
        try {
            const response = await fetch(`${API_BASE_URL}/autores/${autorId}`);
            if (response.ok) {
                autorData = await response.json();
            }
        } catch (error) {
            showError('Erro ao carregar dados do autor');
        }
    }
    
    // Construir formulário
    form.innerHTML = `
        <div class="form-group">
            <label for="autor-nome">Nome</label>
            <input type="text" id="autor-nome" value="${autorData.nome || ''}" required>
        </div>
        <div class="form-group">
            <label for="autor-nacionalidade">Nacionalidade</label>
            <input type="text" id="autor-nacionalidade" value="${autorData.nacionalidade || ''}">
        </div>
    `;
    
    // Mostrar modal
    modal.style.display = 'block';
}

// Modal para livros
async function openLivroModal(livroId = null) {
    editingItem = livroId;
    const modal = document.getElementById('modal');
    const form = document.getElementById('modal-form');
    
    // Configurar título do modal
    document.getElementById('modal-titulo').textContent = 
        livroId ? 'Editar Livro' : 'Novo Livro';
    
    // Carregar dados do livro se for edição
    let livroData = {};
    if (livroId) {
        try {
            const response = await fetch(`${API_BASE_URL}/livros/${livroId}`);
            if (response.ok) {
                livroData = await response.json();
            }
        } catch (error) {
            showError('Erro ao carregar dados do livro');
        }
    }
    
    // Carregar autores para o select
    let autores = [];
    try {
        const response = await fetch(`${API_BASE_URL}/autores`);
        if (response.ok) {
            autores = await response.json();
        }
    } catch (error) {
        console.error('Erro ao carregar autores:', error);
    }
    
    // Construir formulário
    form.innerHTML = `
        <div class="form-group">
            <label for="livro-titulo">Título</label>
            <input type="text" id="livro-titulo" value="${livroData.titulo || ''}" required>
        </div>
        <div class="form-group">
            <label for="livro-isbn">ISBN</label>
            <input type="text" id="livro-isbn" value="${livroData.isbn || ''}">
        </div>
        <div class="form-group">
            <label for="livro-ano">Ano de Publicação</label>
            <input type="number" id="livro-ano" value="${livroData.anoPublicacao || ''}">
        </div>
        <div class="form-group">
            <label for="livro-autor">Autor</label>
            <select id="livro-autor">
                <option value="">Selecione um autor</option>
                ${autores.map(autor => `
                    <option value="${autor.id}" ${livroData.autor && livroData.autor.id === autor.id ? 'selected' : ''}>
                        ${autor.nome}
                    </option>
                `).join('')}
            </select>
        </div>
    `;
    
    // Mostrar modal
    modal.style.display = 'block';
}

// Modais para alunos e empréstimos (estrutura similar)
function openAlunoModal() {
    alert('Funcionalidade de alunos em desenvolvimento');
}

function openEmprestimoModal() {
    alert('Funcionalidade de empréstimos em desenvolvimento');
}

// Salvar dados do modal
async function saveModalData() {
    showLoading();
    
    try {
        let url, method, body;
        
        if (currentTab === 'autores') {
            const nome = document.getElementById('autor-nome').value;
            const nacionalidade = document.getElementById('autor-nacionalidade').value;
            
            body = JSON.stringify({ nome, nacionalidade });
            
            if (editingItem) {
                url = `${API_BASE_URL}/autores/${editingItem}`;
                method = 'PUT';
            } else {
                url = `${API_BASE_URL}/autores`;
                method = 'POST';
            }
        } else if (currentTab === 'livros') {
            const titulo = document.getElementById('livro-titulo').value;
            const isbn = document.getElementById('livro-isbn').value;
            const anoPublicacao = document.getElementById('livro-ano').value;
            const autorId = document.getElementById('livro-autor').value;
            
            body = JSON.stringify({ 
                titulo, 
                isbn, 
                anoPublicacao: parseInt(anoPublicacao),
                autor: autorId ? { id: parseInt(autorId) } : null
            });
            
            if (editingItem) {
                url = `${API_BASE_URL}/livros/${editingItem}`;
                method = 'PUT';
            } else {
                url = `${API_BASE_URL}/livros`;
                method = 'POST';
            }
        }
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: body
        });
        
        if (!response.ok) throw new Error('Erro ao salvar dados');
        
        closeModal();
        
        // Recarregar dados da aba atual
        switch(currentTab) {
            case 'autores':
                loadAutores();
                break;
            case 'livros':
                loadLivros();
                break;
        }
        
    } catch (error) {
        showError('Erro ao salvar: ' + error.message);
    } finally {
        hideLoading();
    }
}

// Fechar modal
function closeModal() {
    document.getElementById('modal').style.display = 'none';
    editingItem = null;
}

// Excluir autor
async function deleteAutor(id) {
    if (!confirm('Tem certeza que deseja excluir este autor?')) return;
    
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/autores/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Erro ao excluir autor');
        
        loadAutores();
    } catch (error) {
        showError('Erro ao excluir autor: ' + error.message);
    } finally {
        hideLoading();
    }
}

// Excluir livro
async function deleteLivro(id) {
    if (!confirm('Tem certeza que deseja excluir este livro?')) return;
    
    showLoading();
    try {
        const response = await fetch(`${API_BASE_URL}/livros/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) throw new Error('Erro ao excluir livro');
        
        loadLivros();
    } catch (error) {
        showError('Erro ao excluir livro: ' + error.message);
    } finally {
        hideLoading();
    }
}

// Utilitários
function showLoading() {
    document.getElementById('loading').style.display = 'flex';
}

function hideLoading() {
    document.getElementById('loading').style.display = 'none';
}

function showError(message) {
    alert('Erro: ' + message);
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}