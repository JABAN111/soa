import React, { useState, useEffect } from 'react';
import { Search, Home, Plus, Edit2, Trash2, Filter, ArrowUpDown, ChevronLeft, ChevronRight, DollarSign, BarChart3 } from 'lucide-react';

const API_BASE = 'http://localhost:23210/flat_service/api';
const AGENCY_BASE = 'http://localhost:8080/api/agency';

const FURNISH_LABELS = {
    DESIGNER: 'Дизайнерская',
    NONE: 'Без мебели',
    BAD: 'Плохая',
    LITTLE: 'Минимальная'
};

const TRANSPORT_LABELS = {
    FEW: 'Мало',
    NONE: 'Нет',
    LITTLE: 'Немного',
    NORMAL: 'Нормальный'
};

const App = () => {
    const [activeTab, setActiveTab] = useState('list');
    const [flats, setFlats] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    // Pagination
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);

    // Filters
    const [filters, setFilters] = useState({
        name: '',
        minArea: '',
        maxArea: '',
        minNumberOfRooms: '',
        maxNumberOfRooms: '',
        furnish: '',
        transport: '',
        minX: '',
        maxX: '',
        minY: '',
        maxY: ''
    });

    // Sorting
    const [sorting, setSorting] = useState({
        sortId: '',
        sortName: '',
        sortArea: '',
        sortNumberOfRooms: ''
    });

    // Form data
    const [formData, setFormData] = useState({
        name: '',
        coordinates: { x: '', y: '' },
        area: '',
        numberOfRooms: '',
        numberOfBathrooms: '',
        furnish: 'NONE',
        transport: 'NONE',
        house: { name: '', year: '', numberOfFloors: '' }
    });

    const [editingId, setEditingId] = useState(null);
    const [utilityData, setUtilityData] = useState(null);
    const [agencyData, setAgencyData] = useState(null);

    useEffect(() => {
        if (activeTab === 'list') {
            fetchFlats();
        }
    }, [page, size, filters, sorting, activeTab]);

    const buildQueryString = () => {
        const params = new URLSearchParams();
        params.append('page', page);
        params.append('size', size);

        Object.entries(filters).forEach(([key, value]) => {
            if (value !== '') params.append(key, value);
        });

        Object.entries(sorting).forEach(([key, value]) => {
            if (value !== '') params.append(key, value);
        });

        return params.toString();
    };

    const fetchFlats = async () => {
        setLoading(true);
        setError(null);
        try {
            const query = buildQueryString();
            const response = await fetch(`${API_BASE}/flats?${query}`);
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка загрузки данных');
            }
            const data = await response.json();
            setFlats(data);
            setTotalPages(Math.ceil(data.length / size));
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(null);

        try {
            const payload = {
                ...formData,
                coordinates: {
                    x: parseInt(formData.coordinates.x),
                    y: parseInt(formData.coordinates.y)
                },
                area: parseInt(formData.area),
                numberOfRooms: parseInt(formData.numberOfRooms),
                numberOfBathrooms: parseInt(formData.numberOfBathrooms),
                house: formData.house.name ? {
                    name: formData.house.name,
                    year: formData.house.year ? parseInt(formData.house.year) : undefined,
                    numberOfFloors: formData.house.numberOfFloors ? parseInt(formData.house.numberOfFloors) : undefined
                } : undefined
            };

            const url = editingId ? `${API_BASE}/flats/${editingId}` : `${API_BASE}/flats`;
            const method = editingId ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка сохранения данных');
            }

            setSuccess(editingId ? 'Квартира успешно обновлена' : 'Квартира успешно создана');
            resetForm();
            setActiveTab('list');
            fetchFlats();
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!confirm('Вы уверены, что хотите удалить эту квартиру?')) return;

        setLoading(true);
        setError(null);
        try {
            const response = await fetch(`${API_BASE}/flats/${id}`, { method: 'DELETE' });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка удаления');
            }
            setSuccess('Квартира успешно удалена');
            fetchFlats();
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (flat) => {
        setFormData({
            name: flat.name,
            coordinates: { x: flat.coordinates.x, y: flat.coordinates.y },
            area: flat.area,
            numberOfRooms: flat.numberOfRooms,
            numberOfBathrooms: flat.numberOfBathrooms,
            furnish: flat.furnish,
            transport: flat.transport || 'NONE',
            house: flat.house || { name: '', year: '', numberOfFloors: '' }
        });
        setEditingId(flat.id);
        setActiveTab('form');
    };

    const resetForm = () => {
        setFormData({
            name: '',
            coordinates: { x: '', y: '' },
            area: '',
            numberOfRooms: '',
            numberOfBathrooms: '',
            furnish: 'NONE',
            transport: 'NONE',
            house: { name: '', year: '', numberOfFloors: '' }
        });
        setEditingId(null);
    };

    const fetchUtilities = async (type) => {
        setLoading(true);
        setError(null);
        try {
            let url = '';
            if (type === 'sum-rooms') {
                url = `${API_BASE}/flats/utils/sum-rooms`;
            } else if (type === 'group-by-rooms') {
                url = `${API_BASE}/flats/utils/group-by-rooms`;
            } else if (type === 'transport-greater') {
                const value = prompt('Введите уровень транспорта (NONE, FEW, LITTLE, NORMAL):');
                if (!value) return;
                url = `${API_BASE}/flats/utils/transport-greater-than/${value}`;
            }

            const response = await fetch(url);
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка получения данных');
            }
            const data = await response.json();
            setUtilityData({ type, data });
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const fetchAgencyData = async (type) => {
        setLoading(true);
        setError(null);
        try {
            let url = '';
            if (type === 'most-expensive') {
                const id1 = prompt('Введите ID первой квартиры:');
                const id2 = prompt('Введите ID второй квартиры:');
                const id3 = prompt('Введите ID третьей квартиры:');
                if (!id1 || !id2 || !id3) return;
                url = `${AGENCY_BASE}/get-most-expensive/${id1}/${id2}/${id3}`;
            } else if (type === 'total-cost') {
                url = `${AGENCY_BASE}/get-total-cost`;
            }

            const response = await fetch(url);
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка получения данных агентства');
            }
            const data = await response.json();
            setAgencyData({ type, data });
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <header className="bg-blue-600 text-white p-4 shadow-lg">
                <div className="max-w-7xl mx-auto px-4">
                    <h1 className="text-2xl font-bold flex items-center gap-2">
                        <Home size={28} />
                        Система управления квартирами
                    </h1>
                </div>
            </header>

            <nav className="bg-white shadow-md">
                <div className="max-w-7xl mx-auto px-4">
                    <div className="flex gap-1 overflow-x-auto">
                        {[
                            { id: 'list', label: 'Список квартир', icon: Search },
                            { id: 'form', label: editingId ? 'Редактировать' : 'Добавить', icon: Plus },
                            { id: 'utilities', label: 'Утилиты', icon: BarChart3 },
                            { id: 'agency', label: 'Агентство', icon: DollarSign }
                        ].map(tab => (
                            <button
                                key={tab.id}
                                onClick={() => setActiveTab(tab.id)}
                                className={`px-4 py-3 font-medium flex items-center gap-2 border-b-2 transition-colors ${
                                    activeTab === tab.id
                                        ? 'border-blue-600 text-blue-600'
                                        : 'border-transparent text-gray-600 hover:text-blue-600'
                                }`}
                            >
                                <tab.icon size={18} />
                                {tab.label}
                            </button>
                        ))}
                    </div>
                </div>
            </nav>

            <main className="max-w-7xl mx-auto p-4">
                {error && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
                        <strong>Ошибка:</strong> {error}
                    </div>
                )}

                {success && (
                    <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
                        {success}
                    </div>
                )}

                {activeTab === 'list' && (
                    <div className="space-y-4">
                        <div className="bg-white rounded-lg shadow p-4">
                            <h2 className="text-lg font-bold mb-3 flex items-center gap-2">
                                <Filter size={20} />
                                Фильтры и сортировка
                            </h2>

                            <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-3">
                                <input
                                    type="text"
                                    placeholder="Название"
                                    value={filters.name}
                                    onChange={(e) => setFilters({ ...filters, name: e.target.value })}
                                    className="border rounded px-3 py-2"
                                />
                                <input
                                    type="number"
                                    placeholder="Мин. площадь"
                                    value={filters.minArea}
                                    onChange={(e) => setFilters({ ...filters, minArea: e.target.value })}
                                    className="border rounded px-3 py-2"
                                />
                                <input
                                    type="number"
                                    placeholder="Макс. площадь"
                                    value={filters.maxArea}
                                    onChange={(e) => setFilters({ ...filters, maxArea: e.target.value })}
                                    className="border rounded px-3 py-2"
                                />
                                <input
                                    type="number"
                                    placeholder="Мин. комнат"
                                    value={filters.minNumberOfRooms}
                                    onChange={(e) => setFilters({ ...filters, minNumberOfRooms: e.target.value })}
                                    className="border rounded px-3 py-2"
                                />
                                <select
                                    value={filters.furnish}
                                    onChange={(e) => setFilters({ ...filters, furnish: e.target.value })}
                                    className="border rounded px-3 py-2"
                                >
                                    <option value="">Мебель: Все</option>
                                    {Object.entries(FURNISH_LABELS).map(([key, label]) => (
                                        <option key={key} value={key}>{label}</option>
                                    ))}
                                </select>
                                <select
                                    value={filters.transport}
                                    onChange={(e) => setFilters({ ...filters, transport: e.target.value })}
                                    className="border rounded px-3 py-2"
                                >
                                    <option value="">Транспорт: Все</option>
                                    {Object.entries(TRANSPORT_LABELS).map(([key, label]) => (
                                        <option key={key} value={key}>{label}</option>
                                    ))}
                                </select>
                                <select
                                    value={sorting.sortName}
                                    onChange={(e) => setSorting({ ...sorting, sortName: e.target.value })}
                                    className="border rounded px-3 py-2"
                                >
                                    <option value="">Сорт. по названию</option>
                                    <option value="asc">А-Я</option>
                                    <option value="desc">Я-А</option>
                                </select>
                                <select
                                    value={sorting.sortArea}
                                    onChange={(e) => setSorting({ ...sorting, sortArea: e.target.value })}
                                    className="border rounded px-3 py-2"
                                >
                                    <option value="">Сорт. по площади</option>
                                    <option value="asc">По возрастанию</option>
                                    <option value="desc">По убыванию</option>
                                </select>
                            </div>

                            <button
                                onClick={() => {
                                    setFilters({
                                        name: '', minArea: '', maxArea: '', minNumberOfRooms: '',
                                        maxNumberOfRooms: '', furnish: '', transport: '', minX: '',
                                        maxX: '', minY: '', maxY: ''
                                    });
                                    setSorting({ sortId: '', sortName: '', sortArea: '', sortNumberOfRooms: '' });
                                }}
                                className="mt-3 px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
                            >
                                Сбросить фильтры
                            </button>
                        </div>

                        {loading ? (
                            <div className="text-center py-8">Загрузка...</div>
                        ) : (
                            <>
                                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                                    {flats.map(flat => (
                                        <div key={flat.id} className="bg-white rounded-lg shadow p-4 hover:shadow-lg transition-shadow">
                                            <div className="flex justify-between items-start mb-3">
                                                <h3 className="text-lg font-bold text-blue-600">{flat.name}</h3>
                                                <span className="text-sm text-gray-500">ID: {flat.id}</span>
                                            </div>

                                            <div className="space-y-2 text-sm">
                                                <p><strong>Площадь:</strong> {flat.area} м²</p>
                                                <p><strong>Комнаты:</strong> {flat.numberOfRooms}</p>
                                                <p><strong>Ванные:</strong> {flat.numberOfBathrooms}</p>
                                                <p><strong>Мебель:</strong> {FURNISH_LABELS[flat.furnish]}</p>
                                                {flat.transport && (
                                                    <p><strong>Транспорт:</strong> {TRANSPORT_LABELS[flat.transport]}</p>
                                                )}
                                                <p><strong>Координаты:</strong> X: {flat.coordinates.x}, Y: {flat.coordinates.y}</p>
                                                {flat.house && (
                                                    <div className="pt-2 border-t">
                                                        <p className="font-semibold">Дом:</p>
                                                        <p className="text-xs">{flat.house.name}</p>
                                                        {flat.house.year && <p className="text-xs">Год: {flat.house.year}</p>}
                                                        {flat.house.numberOfFloors && <p className="text-xs">Этажей: {flat.house.numberOfFloors}</p>}
                                                    </div>
                                                )}
                                                <p className="text-xs text-gray-500">Создано: {flat.creationDate}</p>
                                            </div>

                                            <div className="flex gap-2 mt-4">
                                                <button
                                                    onClick={() => handleEdit(flat)}
                                                    className="flex-1 px-3 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded flex items-center justify-center gap-2"
                                                >
                                                    <Edit2 size={16} />
                                                    Изменить
                                                </button>
                                                <button
                                                    onClick={() => handleDelete(flat.id)}
                                                    className="flex-1 px-3 py-2 bg-red-500 hover:bg-red-600 text-white rounded flex items-center justify-center gap-2"
                                                >
                                                    <Trash2 size={16} />
                                                    Удалить
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>

                                {flats.length === 0 && (
                                    <div className="text-center py-8 text-gray-500">
                                        Квартиры не найдены
                                    </div>
                                )}

                                <div className="flex justify-between items-center mt-6">
                                    <button
                                        onClick={() => setPage(Math.max(0, page - 1))}
                                        disabled={page === 0}
                                        className="px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded disabled:bg-gray-300 flex items-center gap-2"
                                    >
                                        <ChevronLeft size={18} />
                                        Назад
                                    </button>

                                    <span className="text-gray-700">
                    Страница {page + 1} | Показано: {flats.length} квартир
                  </span>

                                    <button
                                        onClick={() => setPage(page + 1)}
                                        disabled={flats.length < size}
                                        className="px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded disabled:bg-gray-300 flex items-center gap-2"
                                    >
                                        Далее
                                        <ChevronRight size={18} />
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                )}

                {activeTab === 'form' && (
                    <div className="bg-white rounded-lg shadow p-6 max-w-2xl mx-auto">
                        <h2 className="text-xl font-bold mb-4">
                            {editingId ? 'Редактировать квартиру' : 'Добавить новую квартиру'}
                        </h2>

                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block font-medium mb-1">Название *</label>
                                <input
                                    type="text"
                                    required
                                    value={formData.name}
                                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                                    className="w-full border rounded px-3 py-2"
                                />
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block font-medium mb-1">Координата X * (max: 324)</label>
                                    <input
                                        type="number"
                                        required
                                        max={324}
                                        value={formData.coordinates.x}
                                        onChange={(e) => setFormData({
                                            ...formData,
                                            coordinates: { ...formData.coordinates, x: e.target.value }
                                        })}
                                        className="w-full border rounded px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block font-medium mb-1">Координата Y * (max: 832)</label>
                                    <input
                                        type="number"
                                        required
                                        max={832}
                                        value={formData.coordinates.y}
                                        onChange={(e) => setFormData({
                                            ...formData,
                                            coordinates: { ...formData.coordinates, y: e.target.value }
                                        })}
                                        className="w-full border rounded px-3 py-2"
                                    />
                                </div>
                            </div>

                            <div className="grid grid-cols-3 gap-4">
                                <div>
                                    <label className="block font-medium mb-1">Площадь * (м²)</label>
                                    <input
                                        type="number"
                                        required
                                        min={1}
                                        value={formData.area}
                                        onChange={(e) => setFormData({ ...formData, area: e.target.value })}
                                        className="w-full border rounded px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block font-medium mb-1">Комнаты *</label>
                                    <input
                                        type="number"
                                        required
                                        min={1}
                                        value={formData.numberOfRooms}
                                        onChange={(e) => setFormData({ ...formData, numberOfRooms: e.target.value })}
                                        className="w-full border rounded px-3 py-2"
                                    />
                                </div>
                                <div>
                                    <label className="block font-medium mb-1">Ванные *</label>
                                    <input
                                        type="number"
                                        required
                                        min={1}
                                        value={formData.numberOfBathrooms}
                                        onChange={(e) => setFormData({ ...formData, numberOfBathrooms: e.target.value })}
                                        className="w-full border rounded px-3 py-2"
                                    />
                                </div>
                            </div>

                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block font-medium mb-1">Мебель *</label>
                                    <select
                                        required
                                        value={formData.furnish}
                                        onChange={(e) => setFormData({ ...formData, furnish: e.target.value })}
                                        className="w-full border rounded px-3 py-2"
                                    >
                                        {Object.entries(FURNISH_LABELS).map(([key, label]) => (
                                            <option key={key} value={key}>{label}</option>
                                        ))}
                                    </select>
                                </div>
                                <div>
                                    <label className="block font-medium mb-1">Транспорт</label>
                                    <select
                                        value={formData.transport}
                                        onChange={(e) => setFormData({ ...formData, transport: e.target.value })}
                                        className="w-full border rounded px-3 py-2"
                                    >
                                        {Object.entries(TRANSPORT_LABELS).map(([key, label]) => (
                                            <option key={key} value={key}>{label}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>

                            <div className="border-t pt-4">
                                <h3 className="font-medium mb-3">Информация о доме (опционально)</h3>
                                <div className="space-y-3">
                                    <div>
                                        <label className="block font-medium mb-1">Название дома</label>
                                        <input
                                            type="text"
                                            value={formData.house.name}
                                            onChange={(e) => setFormData({
                                                ...formData,
                                                house: { ...formData.house, name: e.target.value }
                                            })}
                                            className="w-full border rounded px-3 py-2"
                                        />
                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block font-medium mb-1">Год постройки (min: 1)</label>
                                            <input
                                                type="number"
                                                min={1}
                                                value={formData.house.year}
                                                onChange={(e) => setFormData({
                                                    ...formData,
                                                    house: { ...formData.house, year: e.target.value }
                                                })}
                                                className="w-full border rounded px-3 py-2"
                                            />
                                        </div>
                                        <div>
                                            <label className="block font-medium mb-1">Этажей (min: 1)</label>
                                            <input
                                                type="number"
                                                min={1}
                                                value={formData.house.numberOfFloors}
                                                onChange={(e) => setFormData({
                                                    ...formData,
                                                    house: { ...formData.house, numberOfFloors: e.target.value }
                                                })}
                                                className="w-full border rounded px-3 py-2"
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="flex gap-3 pt-4">
                                <button
                                    type="submit"
                                    disabled={loading}
                                    className="flex-1 px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded disabled:bg-gray-300"
                                >
                                    {loading ? 'Сохранение...' : editingId ? 'Обновить' : 'Создать'}
                                </button>
                                <button
                                    type="button"
                                    onClick={() => {
                                        resetForm();
                                        setActiveTab('list');
                                    }}
                                    className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
                                >
                                    Отмена
                                </button>
                            </div>
                        </form>
                    </div>
                )}

                {activeTab === 'utilities' && (
                    <div className="space-y-4">
                        <div className="bg-white rounded-lg shadow p-6">
                            <h2 className="text-xl font-bold mb-4">Утилиты</h2>

                            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                <button
                                    onClick={() => fetchUtilities('sum-rooms')}
                                    className="p-4 bg-blue-50 hover:bg-blue-100 rounded border border-blue-200"
                                >
                                    <h3 className="font-bold text-blue-700">Сумма комнат</h3>
                                    <p className="text-sm text-gray-600 mt-1">Общее количество комнат во всех квартирах</p>
                                </button>

                                <button
                                    onClick={() => fetchUtilities('group-by-rooms')}
                                    className="p-4 bg-green-50 hover:bg-green-100 rounded border border-green-200"
                                >
                                    <h3 className="font-bold text-green-700">Группировка по комнатам</h3>
                                    <p className="text-sm text-gray-600 mt-1">Количество квартир для каждого значения комнат</p>
                                </button>

                                <button
                                    onClick={() => fetchUtilities('transport-greater')}
                                    className="p-4 bg-purple-50 hover:bg-purple-100 rounded border border-purple-200"
                                >
                                    <h3 className="font-bold text-purple-700">Транспорт лучше</h3>
                                    <p className="text-sm text-gray-600 mt-1">Квартиры с лучшим уровнем транспорта</p>
                                </button>
                            </div>
                        </div>

                        {utilityData && (
                            <div className="bg-white rounded-lg shadow p-6">
                                <h3 className="text-lg font-bold mb-4">Результаты</h3>

                                {utilityData.type === 'sum-rooms' && (
                                    <div className="text-center p-8 bg-blue-50 rounded">
                                        <p className="text-4xl font-bold text-blue-600">{utilityData.data.totalRooms}</p>
                                        <p className="text-gray-600 mt-2">Общее количество комнат</p>
                                    </div>
                                )}

                                {utilityData.type === 'group-by-rooms' && (
                                    <table className="w-full border-collapse">
                                        <thead>
                                        <tr className="bg-gray-100">
                                            <th className="border p-3 text-left">Количество комнат</th>
                                            <th className="border p-3 text-left">Количество квартир</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {Object.entries(utilityData.data).map(([rooms, count]) => (
                                            <tr key={rooms} className="hover:bg-gray-50">
                                                <td className="border p-3">{rooms}</td>
                                                <td className="border p-3 font-semibold">{count}</td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                )}

                                {utilityData.type === 'transport-greater' && (
                                    <div>
                                        <p className="mb-4 text-gray-700">Найдено квартир: {utilityData.data.length}</p>
                                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                                            {utilityData.data.map(flat => (
                                                <div key={flat.id} className="bg-purple-50 rounded-lg p-4 border border-purple-200">
                                                    <h4 className="font-bold text-purple-700">{flat.name}</h4>
                                                    <p className="text-sm mt-2"><strong>ID:</strong> {flat.id}</p>
                                                    <p className="text-sm"><strong>Транспорт:</strong> {TRANSPORT_LABELS[flat.transport]}</p>
                                                    <p className="text-sm"><strong>Комнаты:</strong> {flat.numberOfRooms}</p>
                                                    <p className="text-sm"><strong>Площадь:</strong> {flat.area} м²</p>
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                )}
                            </div>
                        )}
                    </div>
                )}

                {activeTab === 'agency' && (
                    <div className="space-y-4">
                        <div className="bg-white rounded-lg shadow p-6">
                            <h2 className="text-xl font-bold mb-4">Операции агентства</h2>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <button
                                    onClick={() => fetchAgencyData('most-expensive')}
                                    className="p-6 bg-yellow-50 hover:bg-yellow-100 rounded border border-yellow-200"
                                >
                                    <DollarSign className="mx-auto mb-2 text-yellow-600" size={32} />
                                    <h3 className="font-bold text-yellow-700">Самая дорогая из трех</h3>
                                    <p className="text-sm text-gray-600 mt-2">Сравнить три квартиры и найти самую дорогую</p>
                                </button>

                                <button
                                    onClick={() => fetchAgencyData('total-cost')}
                                    className="p-6 bg-emerald-50 hover:bg-emerald-100 rounded border border-emerald-200"
                                >
                                    <BarChart3 className="mx-auto mb-2 text-emerald-600" size={32} />
                                    <h3 className="font-bold text-emerald-700">Общая стоимость</h3>
                                    <p className="text-sm text-gray-600 mt-2">Суммарная стоимость всех квартир</p>
                                </button>
                            </div>
                        </div>

                        {agencyData && (
                            <div className="bg-white rounded-lg shadow p-6">
                                <h3 className="text-lg font-bold mb-4">Результаты</h3>

                                {agencyData.type === 'most-expensive' && (
                                    <div className="bg-yellow-50 rounded-lg p-6 border border-yellow-200">
                                        <h4 className="text-2xl font-bold text-yellow-700 mb-4">
                                            Самая дорогая квартира
                                        </h4>
                                        <div className="grid grid-cols-2 gap-4">
                                            <div>
                                                <p className="text-sm text-gray-600">Название</p>
                                                <p className="font-semibold text-lg">{agencyData.data.name}</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">ID</p>
                                                <p className="font-semibold text-lg">{agencyData.data.id}</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Площадь</p>
                                                <p className="font-semibold text-lg">{agencyData.data.area} м²</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Комнаты</p>
                                                <p className="font-semibold text-lg">{agencyData.data.numberOfRooms}</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Ванные</p>
                                                <p className="font-semibold text-lg">{agencyData.data.numberOfBathrooms}</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Стоимость</p>
                                                <p className="font-bold text-2xl text-yellow-700">
                                                    ${agencyData.data.price?.toLocaleString() || 'N/A'}
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                )}

                                {agencyData.type === 'total-cost' && (
                                    <div className="text-center p-12 bg-emerald-50 rounded-lg border border-emerald-200">
                                        <p className="text-sm text-gray-600 mb-2">Общая стоимость всех квартир</p>
                                        <p className="text-5xl font-bold text-emerald-700 mb-2">
                                            ${agencyData.data.totalCost?.toLocaleString()}
                                        </p>
                                        <p className="text-lg text-gray-600">{agencyData.data.currency}</p>
                                    </div>
                                )}
                            </div>
                        )}
                    </div>
                )}
            </main>

            <footer className="bg-gray-800 text-white text-center p-4 mt-8">
                <p className="text-sm">Система управления квартирами © 2025</p>
            </footer>
        </div>
    );
};

export default App;